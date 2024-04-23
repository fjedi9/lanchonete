package com.vepilef.food.api.v1.model.input;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoInput {

	@ApiModelProperty(required = true)
	@Valid
	@NotNull
	private RestauranteIdInput restaurante;
	
	@Valid
	@NotNull
	private EnderecoInput enderecoEntrega;
	
	@Valid
	@NotNull
	private FormaPagamentoIdInput formaPagamento;
	
	@Valid
	@Size(min = 1)
	@NotNull
	private List<ItemPedidoInput> itens;
	
}
