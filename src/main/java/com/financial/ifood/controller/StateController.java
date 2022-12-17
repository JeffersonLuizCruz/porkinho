package com.financial.ifood.controller;

import java.util.List;

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

import com.financial.ifood.domain.model.State;
import com.financial.ifood.service.StateService;

import javax.validation.Valid;

@RestController
@RequestMapping("states")
public class StateController {

	private final StateService stateService;
	
	public StateController(StateService stateService) {
		this.stateService = stateService;
	}

	@PostMapping
	public ResponseEntity<State> save(@RequestBody @Valid State state){
		return ResponseEntity.status(HttpStatus.CREATED).body(stateService.save(state));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<State> update(@PathVariable Long id, @RequestBody State state){
		return ResponseEntity.ok(stateService.update(id, state));
	}
	
	@GetMapping
	public ResponseEntity<List<State>> findAll(){
		return ResponseEntity.ok(stateService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<State> findById(@PathVariable Long id){
		return ResponseEntity.ok(stateService.findById(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		stateService.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
