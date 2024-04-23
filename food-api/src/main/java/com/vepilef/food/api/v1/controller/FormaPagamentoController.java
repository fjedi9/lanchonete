package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.assembler.FormaPagamentoInputDisassembler;
import com.vepilef.food.api.v1.assembler.FormaPagamentoModelAssembler;
import com.vepilef.food.api.v1.model.FormaPagamentoModel;
import com.vepilef.food.api.v1.model.input.FormaPagamentoInput;
import com.vepilef.food.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.domain.exception.EstadoNaoEncontradoException;
import com.vepilef.food.domain.exception.NegocioException;
import com.vepilef.food.domain.model.FormaPagamento;
import com.vepilef.food.domain.repository.FormaPagamentoRepository;
import com.vepilef.food.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/v1/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;

    @CheckSecurity.FormaPagamento.PodeConsultar
    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
        if (dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }
        if (request.checkNotModified(eTag)) {
            return null;
        }

        List<FormaPagamento> todasFormasPagamento = formaPagamentoRepository.findAll();
        CollectionModel<FormaPagamentoModel> formasPagamentoModel = formaPagamentoModelAssembler
                .toCollectionModel(todasFormasPagamento);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(formasPagamentoModel);
    }

    @CheckSecurity.FormaPagamento.PodeConsultar
    @Override
    @GetMapping("/{idFormaPagamento}")
    public ResponseEntity<FormaPagamentoModel> buscar(ServletWebRequest request, @PathVariable Long idFormaPagamento) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";

        OffsetDateTime dataAtualizacao = formaPagamentoRepository
                .getDataUltimaAtualizacaoById(idFormaPagamento);
        if (dataAtualizacao != null) {
            eTag = String.valueOf(dataAtualizacao.toEpochSecond());
        }
        if (request.checkNotModified(eTag)) {
            return null;
        }

        FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(idFormaPagamento);
        FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler.toModel(formaPagamento);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .eTag(eTag)
                .body(formaPagamentoModel);
    }

    @CheckSecurity.FormaPagamento.PodeEditar
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel salvar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        try {
            FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
            return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamento));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.FormaPagamento.PodeEditar
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FormaPagamentoModel atualizar(@PathVariable Long id, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        try {
            FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscarOuFalhar(id);

            formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

            formaPagamentoAtual = cadastroFormaPagamentoService.salvar(formaPagamentoAtual);

            return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @CheckSecurity.FormaPagamento.PodeEditar
    @Override
    @DeleteMapping("/{idFormaPagamento}")
    public void remover(@PathVariable Long idFormaPagamento) {
        cadastroFormaPagamentoService.remover(idFormaPagamento);
    }

}
