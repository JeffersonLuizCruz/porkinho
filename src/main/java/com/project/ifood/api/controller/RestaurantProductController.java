package com.project.ifood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ifood.api.controller.mapper.ProductMapper;
import com.project.ifood.api.controller.mapper.dto.ProductResponseDTO;
import com.project.ifood.api.controller.mapper.dto.ProductResume;
import com.project.ifood.domain.model.Product;
import com.project.ifood.domain.model.Restaurant;
import com.project.ifood.domain.service.ProductService;
import com.project.ifood.domain.service.RestaurantService;
import com.project.ifood.domain.service.exception.ConstraintViolationService;

import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/products")
public class RestaurantProductController {

	private final RestaurantService restaurantService;
	private final ProductService productService;
	private final ProductMapper productMapper;

	@PostMapping
	public ResponseEntity<ProductResponseDTO> save(@PathVariable Long restaurantId, @RequestBody @Valid ProductResume productResume){
		Restaurant restaurantEntity = restaurantService.checkIfRestaurantExists(restaurantId);
		
		Product modelProduct = productMapper.toModel(productResume);
		modelProduct.setRestaurant(restaurantEntity);
		
		Product productEntity = productService.save(modelProduct);

		
		return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(productEntity));
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<ProductResponseDTO> update(@PathVariable Long restaurantId,@PathVariable Long productId, @RequestBody @Valid ProductResume productResume){
		Restaurant restaurantEntity = restaurantService.checkIfRestaurantExists(restaurantId);

		Product modelProduct = productMapper.toModel(productResume);
		modelProduct.setRestaurant(restaurantEntity);
		
		Product productEntity = productService.update(productId, modelProduct);
		return ResponseEntity.ok(productMapper.toDTO(productEntity));
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponseDTO>> findByProductsAll(@PathVariable Long restaurantId) {
		Restaurant restaurant = restaurantService.checkIfRestaurantExists(restaurantId);
		
		List<ProductResponseDTO> listProductDTO = restaurant.getProducts().stream()
		.map(product -> productMapper.toDTO(product))
		.collect(Collectors.toList());
		
		return ResponseEntity.ok(listProductDTO);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponseDTO> findByProduct(@PathVariable Long restaurantId, @PathVariable Long productId) {
		
		/***
		 * Exemplo simplificado de implementação do método:
		 * 
		 * 	Optional<Product> productOptional = productRepository.findByIdProduct(restaurantId, productId);
		 *	if(!productOptional.isPresent()) throw new ConstraintViolationService("Objeto não encontrado");
		 *
		 *	return ResponseEntity.ok(productMapper.toDTO(productOptional.get()));
		 *	 
		 * */
		
		Restaurant restaurant = restaurantService.checkIfRestaurantExists(restaurantId);
		
		List<ProductResponseDTO> listProductDTO = restaurant.getProducts().stream()
				.map(product -> productMapper.toDTO(product))
				.collect(Collectors.toList());

		ProductResponseDTO productDTO = listProductDTO.stream()
				.filter(p -> p.getId() == productId)
				.findFirst()
				.orElseThrow(() -> new ConstraintViolationService(String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d", 
						restaurantId, productId)));
		
		return ResponseEntity.ok(productDTO);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		productService.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
