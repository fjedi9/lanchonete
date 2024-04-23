package com.vepilef.food.api.v1.model.input;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.vepilef.food.core.validation.TaxaFrete;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteInput {

	@ApiModelProperty(example = "Thai Gourmet", required = true)
	@NotBlank
	private String nome;

	@ApiModelProperty(example = "10.00", required = true)
	@NotNull
	@TaxaFrete
	private BigDecimal taxaFrete;

	@Valid
	@NotNull
	private CozinhaIdInput cozinha;

	@Valid
	@NotNull
	private EnderecoInput endereco;
	
}
