package com.vepilef.food.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = -4789090968896555220L;

	public RestauranteNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public RestauranteNaoEncontradoException(Long idRestaurante) {
		this(String.format("Não existe cadastro de restaurante com código %d", idRestaurante));
	}

}
