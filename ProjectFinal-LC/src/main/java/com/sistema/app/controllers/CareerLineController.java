package com.sistema.app.controllers;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import com.sistema.app.models.CareerLine;
import com.sistema.app.service.CareerLineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/career")
public class CareerLineController {

	@Autowired
	private CareerLineService service;

	@GetMapping
	public Mono<ResponseEntity<Flux<CareerLine>>> findAllCareer(){
		return Mono.justOrEmpty(
				ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.findAllCareer())
				);
	}
	
	
	@GetMapping("select2/{codCareer}")
	public Mono<ResponseEntity<CareerLine>> viewCareer(@PathVariable String codCareer){
		return service.findByCodCareer(codCareer).map(p-> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
			
	}
	
	@GetMapping("select/{id}")
	public Mono<ResponseEntity<CareerLine>> viewCareerId(@PathVariable String id){
		return service.findById(id).map(p-> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
			
	}
	
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> saveCareer(@Valid @RequestBody Mono<CareerLine> monoCareer){
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		return monoCareer.flatMap(career -> {
			return service.saveCareer(career).map(c-> {
				response.put("CareerLine", c);
				response.put("mensaje", "Carrera línea registrado con éxito");
				response.put("timestamp", new Date());
				return ResponseEntity
					.created(URI.create("/api/career/".concat(c.getId())))
					.contentType(MediaType.APPLICATION_JSON)
					.body(response);
				});
			
		}).onErrorResume(r -> {
			return Mono.just(r).cast(WebExchangeBindException.class)
					.flatMap(e -> Mono.just(e.getFieldErrors()))
					.flatMapMany(Flux::fromIterable)
					.map(fieldError -> "El campo "+fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collectList()
					.flatMap(list -> {
						response.put("errors", list);
						response.put("timestamp", new Date());
						response.put("status", HttpStatus.BAD_REQUEST.value());
						return Mono.just(ResponseEntity.badRequest().body(response));
					});		
		});
	}
	
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<CareerLine>> updateCareer(@RequestBody CareerLine career, @PathVariable String id)
	{
		return service.findById(id)
				.flatMap(c -> {
					c.setCodCareer(career.getCodCareer());
					c.setNameCareer(career.getNameCareer());
					return service.saveCareer(c);
				}).map(s -> ResponseEntity.created(URI.create("/api/career/".concat(s.getId())))
				  .contentType(MediaType.APPLICATION_JSON)
				  .body(s))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}	
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteCourse(@PathVariable String id)
	{
		return service.findById(id).flatMap(c -> {
			return service.deleteCareer(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));		
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}
}
