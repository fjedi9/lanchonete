package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.assembler.EstadoInputDisassembler;
import com.vepilef.food.api.v1.assembler.EstadoModelAssembler;
import com.vepilef.food.api.v1.model.EstadoModel;
import com.vepilef.food.api.v1.model.input.EstadoInput;
import com.vepilef.food.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.domain.model.Estado;
import com.vepilef.food.domain.repository.EstadoRepository;
import com.vepilef.food.domain.service.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EstadoControllerOpenApi {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstadoService;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;

	@CheckSecurity.Estados.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<EstadoModel> listar() {
		return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
	}

	@CheckSecurity.Estados.PodeConsultar
	@Override
	@GetMapping("/{idEstado}")
	public EstadoModel buscar(@PathVariable Long idEstado) {
		return estadoModelAssembler.toModel(cadastroEstadoService.buscarOuFalhar(idEstado));
	}

	@CheckSecurity.Estados.PodeEditar
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel salvar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
		
		estado = cadastroEstadoService.salvar(estado);
		
		return estadoModelAssembler.toModel(estado);
	}

	@CheckSecurity.Estados.PodeEditar
	@Override
	@PutMapping("/{idEstado}")
	@ResponseStatus(HttpStatus.OK)
	public EstadoModel atualizar(@PathVariable Long idEstado,
			@RequestBody @Valid EstadoInput estadoInput) {
		
		Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(idEstado);
		
		estadoInputDisassembler.copoyToDomainObject(estadoInput, estadoAtual);
		
		estadoAtual = cadastroEstadoService.salvar(estadoAtual);
				
		return estadoModelAssembler.toModel(estadoAtual);
	}

	@CheckSecurity.Estados.PodeEditar
	@Override
	@DeleteMapping("/{idEstado}")
	public void remover(@PathVariable Long idEstado) {
			cadastroEstadoService.remover(idEstado);
	}
	
}
