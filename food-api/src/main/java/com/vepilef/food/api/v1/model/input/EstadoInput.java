package com.vepilef.food.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoInput {

	@ApiModelProperty(example = "Ceará", required = true)
	@NotBlank
	private String nome;
	
}
