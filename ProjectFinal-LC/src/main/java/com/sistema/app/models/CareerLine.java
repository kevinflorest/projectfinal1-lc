package com.sistema.app.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="career")
public class CareerLine {
	
	@Id
	private String id;
	
	@NotEmpty
	private String codCareer;
	@NotEmpty
	private String nameCareer;

}
