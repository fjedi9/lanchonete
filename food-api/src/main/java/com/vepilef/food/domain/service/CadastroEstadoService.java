package com.vepilef.food.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vepilef.food.domain.exception.EntidadeEmUsoException;
import com.vepilef.food.domain.exception.EstadoNaoEncontradoException;
import com.vepilef.food.domain.model.Estado;
import com.vepilef.food.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removido, pois está em uso.";
	
	@Autowired
	private EstadoRepository estadoRepository;

	@Transactional
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}

	@Transactional
	public void remover(Long idEstado) {
		try {
			estadoRepository.deleteById(idEstado);
			estadoRepository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(idEstado);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_ESTADO_EM_USO, idEstado));
		}
	}

	public Estado buscarOuFalhar(Long idEstado) {
		return estadoRepository.findById(idEstado).orElseThrow(
				() -> new EstadoNaoEncontradoException(idEstado));
	}

}
