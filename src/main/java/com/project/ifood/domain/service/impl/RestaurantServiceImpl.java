package com.project.ifood.domain.service.impl;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ifood.domain.model.City;
import com.project.ifood.domain.model.Kitchen;
import com.project.ifood.domain.model.Restaurant;
import com.project.ifood.domain.repositoy.RestaurantRepository;
import com.project.ifood.domain.service.CityService;
import com.project.ifood.domain.service.KitchenService;
import com.project.ifood.domain.service.RestaurantService;
import com.project.ifood.domain.service.exception.ConstraintViolationService;
import com.project.ifood.domain.service.exception.NotFoundExceptionService;

@Service
public class RestaurantServiceImpl implements RestaurantService{

	private final RestaurantRepository restaurantRepository;
	private final KitchenService kitchenService;
	private final CityService cityService;
	
	public RestaurantServiceImpl(RestaurantRepository restaurantRepository, KitchenService kitchenService, CityService cityService) {
		this.restaurantRepository = restaurantRepository;
		this.kitchenService = kitchenService;
		this.cityService = cityService;
	}


	private final String CONSTRAINT_VALIDATION_MESSAGE = "Restaurante de código '%d' não pode ser removida, pois está em uso";
	private final String NOT_FOUND_MESSAGE = "Restaurante de código '%d' não encontrado.";

	@Override
	public Restaurant save(Restaurant restaurant) {
		Kitchen kitchenEntity = kitchenService.checkIfKitchenExists(restaurant.getKitchen().getId());
		City cityEntity = cityService.checkIfCityExists(restaurant.getAddress().getCity().getId());

		restaurant.setKitchen(kitchenEntity);
		restaurant.getAddress().setCity(cityEntity);
		
		return restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant update(Long id, Restaurant restaurant) {
		Restaurant restaurantEntity = checkIfRestaurantExists(id);
		kitchenService.checkIfKitchenExists(restaurant.getKitchen().getId());
		
		restaurant.setId(id);
		restaurant.setCreateAt(restaurantEntity.getCreateAt());
		restaurant.setKitchen(restaurant.getKitchen());
		
		return restaurantRepository.save(restaurant);
	}

	@Override
	public List<Restaurant> findAll() {
		return restaurantRepository.findAll();
	}

	@Override
	public Restaurant findById(Long id) {
		return checkIfRestaurantExists(id);
	}

	@Override
	public void deleteById(Long id) {

		try {
			Restaurant restaurantEntity = checkIfRestaurantExists(id);
			restaurantRepository.delete(restaurantEntity);
			restaurantRepository.flush();
		}catch (DataIntegrityViolationException | ConstraintViolationException e) {
			throw new ConstraintViolationService(String.format(CONSTRAINT_VALIDATION_MESSAGE, id));
		}
	}
	
	@Override
	public Restaurant checkIfRestaurantExists(Long id) {
		return restaurantRepository.findById(id).orElseThrow(() ->
				new NotFoundExceptionService(String.format(NOT_FOUND_MESSAGE, id)));
	}

	@Override
	public void isActive(Long id) {
		Restaurant restaurantEntity = checkIfRestaurantExists(id);
		restaurantEntity.setIsActive(true);
		restaurantRepository.save(restaurantEntity);
		
	}

	@Transactional
	@Override
	public void isDisable(Long id) {
		Restaurant restaurantEntity = checkIfRestaurantExists(id);
		restaurantEntity.setIsActive(false);
		restaurantRepository.save(restaurantEntity);
		
	}
	
	

}
