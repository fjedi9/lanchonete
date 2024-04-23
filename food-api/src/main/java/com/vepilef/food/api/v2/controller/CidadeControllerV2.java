package com.vepilef.food.api.v2.controller;

import com.vepilef.food.api.ResourceUriHelper;
import com.vepilef.food.api.v2.assembler.CidadeInputDisassemblerV2;
import com.vepilef.food.api.v2.assembler.CidadeModelAssemblerV2;
import com.vepilef.food.api.v2.model.CidadeModelV2;
import com.vepilef.food.api.v2.model.input.CidadeInputV2;
import com.vepilef.food.api.v2.openapi.controller.CidadeControllerV2OpenApi;
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

@RestController
@RequestMapping(path = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 implements CidadeControllerV2OpenApi {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssemblerV2 cidadeModelAssemblerV2;

    @Autowired
    private CidadeInputDisassemblerV2 cidadeInputDisassemblerV2;

    @GetMapping
    public CollectionModel<CidadeModelV2> listar() {
        return cidadeModelAssemblerV2.toCollectionModel(cidadeRepository.findAll());
    }

    @CheckSecurity.Cidades.PodeConsultar
    @GetMapping("/{idCidade}")
    public CidadeModelV2 buscar(@PathVariable Long idCidade) {

        Cidade cidade = cadastroCidadeService.buscarOuFalhar(idCidade);
        return cidadeModelAssemblerV2.toModel(cidade);
    }

    @CheckSecurity.Cidades.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModelV2 salvar(@RequestBody @Valid CidadeInputV2 cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassemblerV2.toDomainObject(cidadeInput);
            cidade = cadastroCidadeService.salvar(cidade);
            CidadeModelV2 cidadeModel = cidadeModelAssemblerV2.toModel(cidade);

            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getIdCidade());

            return cidadeModel;
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CidadeModelV2 atualizar(@PathVariable Long id,
                                 @RequestBody @Valid CidadeInputV2 cidadeInputV2) {

        try {
            Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

            cidadeInputDisassemblerV2.copyToDomainObject(cidadeInputV2, cidadeAtual);

            cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);

            return cidadeModelAssemblerV2.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @DeleteMapping("/{idCidade}")
    public void remover(@PathVariable Long idCidade) {
        cadastroCidadeService.remover(idCidade);
    }

}
