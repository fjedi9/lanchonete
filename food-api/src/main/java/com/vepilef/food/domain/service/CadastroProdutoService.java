package com.vepilef.food.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vepilef.food.domain.exception.ProdutoNaoEncontradoException;
import com.vepilef.food.domain.model.Produto;
import com.vepilef.food.domain.model.Restaurante;
import com.vepilef.food.domain.repository.ProdutoRepository;

@Service
public class CadastroProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Transactional
	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}
		
	public Produto buscarOuFalhar(Long idRestaurante, Long idProduto) {
		return produtoRepository.findById(idRestaurante, idProduto)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(idRestaurante, idProduto));
	}

	public List<Produto> findByRestaurante(Restaurante restaurante) {
		return produtoRepository.findByRestaurante(restaurante);
	}
	
}
