package com.vepilef.food.api.v1.model.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioInput {

	@ApiModelProperty(example = "João da Silva", required = true)
	@NotBlank
	private String nome;

	@ApiModelProperty(example = "joao.ger@algafood.com.br", required = true)
	@NotBlank
	@Email
	private String email;

}