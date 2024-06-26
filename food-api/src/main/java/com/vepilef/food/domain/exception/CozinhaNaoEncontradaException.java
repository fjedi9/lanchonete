package com.vepilef.food.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = -4789090968896555220L;

	public CozinhaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public CozinhaNaoEncontradaException(Long idCozinha) {
		this(String.format("Não existe cadastro de cozinha com código %d", idCozinha));
	}

}
