package com.project.ifood.domain.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.ifood.controller.dto.response.ProductResponseDTO;
import com.project.ifood.controller.dto.resume.ProductResume;
import com.project.ifood.controller.mapper.ProductMapper;
import com.project.ifood.domain.model.Product;
import com.project.ifood.domain.model.Restaurant;
import com.project.ifood.domain.service.ProductService;
import com.project.ifood.domain.service.RestaurantByProductService;
import com.project.ifood.domain.service.RestaurantService;
import com.project.ifood.domain.service.exception.ConstraintViolationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantByProductServiceImpl implements RestaurantByProductService {

	private final RestaurantService restaurantService;
	private final ProductService productService;
	private final ProductMapper productMapper;

	@Override
	public Product saveRestaurantByProduct(Long restaurantId, ProductResume productResume) {
		Restaurant restaurantEntity = restaurantService.checkIfRestaurantExists(restaurantId);

		Product modelProduct = productMapper.toModel(productResume);
		modelProduct.setRestaurant(restaurantEntity);

		Product productEntity = productService.save(modelProduct);
		return productEntity;
	}

	@Override
	public ProductResponseDTO verifyIfExistRestaurantByProduct(Long restaurantId, Long productId) {
		Restaurant restaurant = restaurantService.checkIfRestaurantExists(restaurantId);

		List<ProductResponseDTO> listProductDTO = restaurant.getProducts().stream()
				.map(product -> productMapper.toDTO(product)).collect(Collectors.toList());

		ProductResponseDTO productDTO = listProductDTO.stream().filter(p -> p.getId() == productId).findFirst()
				.orElseThrow(() -> new ConstraintViolationService(
						String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d",
								productId, restaurantId)));
		return productDTO;
	}

}
