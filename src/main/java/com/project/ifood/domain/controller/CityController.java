package com.project.ifood.domain.controller;

import java.util.List;

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

import com.project.ifood.domain.controller.mapper.CityMapper;
import com.project.ifood.domain.controller.mapper.dto.CityDTO;
import com.project.ifood.domain.model.City;
import com.project.ifood.domain.service.CityService;

@RestController
@RequestMapping("cities")
public class CityController {

	private final CityService cityService;
	private final CityMapper cityMapper;
	
	public CityController(CityService cityService, CityMapper cityMapper) {
		this.cityService = cityService;
		this.cityMapper = cityMapper;
	}

	@PostMapping
	public ResponseEntity<City> save (@RequestBody @Valid CityDTO dto){
		City city = cityMapper.toModel(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(cityService.save(city));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<City> update (@PathVariable Long id, @RequestBody @Valid CityDTO dto){
		City city = cityMapper.toModel(dto);
		return ResponseEntity.ok(cityService.update(id, city));
	}
	
	@GetMapping
	public ResponseEntity<List<City>> findAll(){
		return ResponseEntity.ok(cityService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<City> findById(@PathVariable Long id){
		return ResponseEntity.ok(cityService.findById(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		cityService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
