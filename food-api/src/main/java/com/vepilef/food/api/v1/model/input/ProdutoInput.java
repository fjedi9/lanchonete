package com.vepilef.food.api.v1.model.input;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoInput {

	@ApiModelProperty(example = "Espetinho de Cupim", required = true)
	@NotBlank
	private String nome;

	@ApiModelProperty(example = "Acompanha farinha, mandioca e vinagrete", required = true)
	@NotBlank
	private String descricao;

	@ApiModelProperty(example = "12.50", required = true)
	@NotNull
	@PositiveOrZero
	private BigDecimal preco;

	@ApiModelProperty(example = "true", required = true)
	@NotNull
	private Boolean ativo;
	
}
