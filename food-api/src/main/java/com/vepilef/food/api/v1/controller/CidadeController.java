package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.ResourceUriHelper;
import com.vepilef.food.api.v1.assembler.CidadeInputDisassembler;
import com.vepilef.food.api.v1.assembler.CidadeModelAssembler;
import com.vepilef.food.api.v1.model.CidadeModel;
import com.vepilef.food.api.v1.model.input.CidadeInput;
import com.vepilef.food.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.domain.exception.EstadoNaoEncontradoException;
import com.vepilef.food.domain.exception.NegocioException;
import com.vepilef.food.domain.model.Cidade;
import com.vepilef.food.domain.repository.CidadeRepository;
import com.vepilef.food.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Deprecated
@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;

    @CheckSecurity.Cidades.PodeConsultar
    @GetMapping
    public CollectionModel<CidadeModel> listar() {
        return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
    }

    @CheckSecurity.Cidades.PodeConsultar
    @Override
    @GetMapping("/{idCidade}")
    public CidadeModel buscar(@PathVariable Long idCidade) {

        Cidade cidade = cadastroCidadeService.buscarOuFalhar(idCidade);
        return cidadeModelAssembler.toModel(cidade);
    }

    @CheckSecurity.Cidades.PodeEditar
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel salvar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
            cidade = cadastroCidadeService.salvar(cidade);
            CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());

            return cidadeModel;
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e); // Alterando código de erro para 400
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CidadeModel atualizar(@PathVariable Long id,
                                 @RequestBody @Valid CidadeInput cidadeInput) {

        try {
            Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {

            throw new NegocioException(e.getMessage());
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @Override
    @DeleteMapping("/{idCidade}")
    public void remover(@PathVariable Long idCidade) {
        cadastroCidadeService.remover(idCidade);
    }

}
