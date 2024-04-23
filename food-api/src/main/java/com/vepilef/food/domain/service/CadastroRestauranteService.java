package com.vepilef.food.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vepilef.food.domain.exception.EntidadeEmUsoException;
import com.vepilef.food.domain.exception.RestauranteNaoEncontradoException;
import com.vepilef.food.domain.model.Cidade;
import com.vepilef.food.domain.model.Cozinha;
import com.vepilef.food.domain.model.FormaPagamento;
import com.vepilef.food.domain.model.Restaurante;
import com.vepilef.food.domain.model.Usuario;
import com.vepilef.food.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser removido, pois está em uso.";

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	@Autowired
	CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	CadastroFormaPagamentoService cadastroFormaPagamentoService;

	@Autowired
	CadastroUsuarioService cadastroUsuarioService;

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long idCozinha = restaurante.getCozinha().getId();
		Long idCidade = restaurante.getEndereco().getCidade().getId();
		
		// Validando se existem cozinha e cidade com o id informado
		Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(idCozinha);
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(idCidade);
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);

		return restauranteRepository.save(restaurante);
	}

	@Transactional
	public void remover(Long idRestaurante) {
		try {
			restauranteRepository.deleteById(idRestaurante);
			restauranteRepository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(idRestaurante);
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_RESTAURANTE_EM_USO, idRestaurante));
		}
	}
	
	@Transactional
	public void ativar(Long idRestaurante) {
		Restaurante restauranteAtual = buscarOuFalhar(idRestaurante);
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long idRestaurante) {
		Restaurante restauranteAtual = buscarOuFalhar(idRestaurante);
		restauranteAtual.inativar();
	}

	@Transactional // Transação global para realização de rollback caso haja falha em algum item da lista
	public void ativar(List<Long> idsRestaurantes) {
		idsRestaurantes.forEach(this::ativar);
	}
	
	@Transactional
	public void inativar(List<Long> idsRestaurantes) {
		idsRestaurantes.forEach(this::inativar);
	}

	public Restaurante buscarOuFalhar(Long idRestaurante) {
		return restauranteRepository.findById(idRestaurante)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(idRestaurante));
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long idRestaurante, Long idFormaPagamento) {
		Restaurante restaurante = buscarOuFalhar(idRestaurante);

		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(idFormaPagamento);
		
		restaurante.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long idRestaurante, Long idFormaPagamento) {
		Restaurante restaurante = buscarOuFalhar(idRestaurante);
		
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(idFormaPagamento);
		
		restaurante.adicionarFormaPagamento(formaPagamento);
	}

	@Transactional
	public void abrir(Long idRestaurante) {
	    Restaurante restauranteAtual = buscarOuFalhar(idRestaurante);
	    
	    restauranteAtual.abrir();
	}

	@Transactional
	public void fechar(Long idRestaurante) {
	    Restaurante restauranteAtual = buscarOuFalhar(idRestaurante);
	    
	    restauranteAtual.fechar();
	}

	@Transactional
	public void desassociarResponsavel(Long idRestaurante, Long idUsuario) {
		Restaurante restaurante = buscarOuFalhar(idRestaurante);
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(idUsuario);
		
		restaurante.desassociarResponsavel(usuario);
	}
	
	@Transactional
	public void associarResponsavel(Long idRestaurante, Long idUsuario) {
		Restaurante restaurante = buscarOuFalhar(idRestaurante);
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(idUsuario);
		
		restaurante.associarResponsavel(usuario);
	}
	
}
