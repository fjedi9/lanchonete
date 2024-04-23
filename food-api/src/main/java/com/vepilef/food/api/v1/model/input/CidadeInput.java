package com.vepilef.food.api.v1.model.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeInput {

	@ApiModelProperty(example = "Fortaleza", required = true)
	@NotBlank
	private String nome;

	@Valid
	@NotNull
	private EstadoIdInput estado;

}
