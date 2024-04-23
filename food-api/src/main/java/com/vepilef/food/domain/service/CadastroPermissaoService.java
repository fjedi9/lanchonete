package com.vepilef.food.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vepilef.food.domain.exception.PermissaoNaoEncontradaException;
import com.vepilef.food.domain.model.Permissao;
import com.vepilef.food.domain.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService {

	@Autowired
	PermissaoRepository permissaoRepository;

	public Permissao buscarOuFalhar(Long idPermissao) {
		return permissaoRepository.findById(idPermissao)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(idPermissao));
	}
	
}
