package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.vepilef.food.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.vepilef.food.api.v1.assembler.RestauranteInputDisassembler;
import com.vepilef.food.api.v1.assembler.RestauranteModelAssembler;
import com.vepilef.food.api.v1.model.RestauranteApenasNomeModel;
import com.vepilef.food.api.v1.model.RestauranteBasicoModel;
import com.vepilef.food.api.v1.model.RestauranteModel;
import com.vepilef.food.api.v1.model.input.RestauranteInput;
import com.vepilef.food.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.domain.exception.CidadeNaoEncontradaException;
import com.vepilef.food.domain.exception.CozinhaNaoEncontradaException;
import com.vepilef.food.domain.exception.NegocioException;
import com.vepilef.food.domain.exception.RestauranteNaoEncontradoException;
import com.vepilef.food.domain.model.Restaurante;
import com.vepilef.food.domain.repository.RestauranteRepository;
import com.vepilef.food.domain.service.CadastroRestauranteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

    @Autowired
    private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

    @Autowired
    private RestauranteInputDisassembler restauranteInputDisassembler;

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping
    public CollectionModel<RestauranteBasicoModel> listar() {
        log.info("Log before query");
        List<Restaurante> restaurantes = restauranteRepository.findAll();

        return restauranteBasicoModelAssembler.toCollectionModel(restaurantes);
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping(params = "projecao=apenas-nome")
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
        return restauranteApenasNomeModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping("/{idRestaurante}")
    public RestauranteModel buscar(@PathVariable Long idRestaurante) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(idRestaurante);
        return restauranteModelAssembler.toModel(restaurante);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteModel salvar(@RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
            return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restaurante));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/{idRestaurante}")
    @ResponseStatus(HttpStatus.OK)
    public RestauranteModel atualizar(@PathVariable Long idRestaurante, @RequestBody @Valid RestauranteInput restauranteInput) {
        try {

            Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(idRestaurante);

            restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

            return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @DeleteMapping("/{idRestaurante}")
    public void remover(@PathVariable Long idRestaurante) {
        cadastroRestauranteService.remover(idRestaurante);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/{idRestaurante}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> ativar(@PathVariable Long idRestaurante) {
        // Opção por PUT por ser um método idenpotente, ainda que executado várias vezes provoca o mesmo resultado
        cadastroRestauranteService.ativar(idRestaurante);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @DeleteMapping("/{idRestaurante}/inativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> inativar(@PathVariable Long idRestaurante) {
        cadastroRestauranteService.inativar(idRestaurante);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> idsRestaurantes) {
        try {
            cadastroRestauranteService.ativar(idsRestaurantes);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> idsRestaurantes) {
        try {
            cadastroRestauranteService.inativar(idsRestaurantes);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{idRestaurante}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> abrir(@PathVariable Long idRestaurante) {
        cadastroRestauranteService.abrir(idRestaurante);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{idRestaurante}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> fechar(@PathVariable Long idRestaurante) {
        cadastroRestauranteService.fechar(idRestaurante);

        return ResponseEntity.noContent().build();
    }
}
