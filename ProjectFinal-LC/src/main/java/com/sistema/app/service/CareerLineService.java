package com.sistema.app.service;

import com.sistema.app.models.CareerLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CareerLineService {


	Flux<CareerLine> findAllCareer();
	
	Mono<CareerLine> findById(String id);
	
	Mono<CareerLine> findByCodCareer(String codCareer);
	
	Mono<CareerLine> saveCareer(CareerLine career);
		
	Mono<Void> deleteCareer(CareerLine career);
	
	
}
