package com.sistema.app.dao;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.sistema.app.models.CareerLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CareerLineDAO extends ReactiveMongoRepository<CareerLine, String> {

	Mono<CareerLine> findByCodCareer(String codCareer);
	
	
}
