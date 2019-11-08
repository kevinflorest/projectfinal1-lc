package com.sistema.app.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema.app.dao.CareerLineDAO;
import com.sistema.app.models.CareerLine;
import com.sistema.app.service.CareerLineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CareerLineServiceImpl implements CareerLineService {

	@Autowired
	private CareerLineDAO cdao;

	
	@Override
	public Flux<CareerLine> findAllCareer() {
		return cdao.findAll();
	}

	@Override
	public Mono<CareerLine> findById(String id) {
		return cdao.findById(id);
	}

	@Override
	public Mono<CareerLine> saveCareer(CareerLine career) {
		Mono<CareerLine> c = Mono.just(career);
		return c.flatMap(response -> {
			
			Mono<Boolean> c1 = cdao.findByCodCareer(response.getCodCareer()).hasElement();
	
			return c1.flatMap(c2 -> {
				System.out.println(c2);
				if(c2)
				{
					throw new RuntimeException("Ya existe la línea de carrera");
				}
				else
				{
					return cdao.save(career);
				}
			});
			
			
		});
	
	}

	@Override
	public Mono<Void> deleteCareer(CareerLine career) {
		return cdao.delete(career);
	}

	@Override
	public Mono<CareerLine> findByCodCareer(String codCareer) {
		return cdao.findByCodCareer(codCareer);
	}

	
}
 