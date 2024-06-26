package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.FoodLinks;
import com.vepilef.food.api.v1.assembler.FormaPagamentoModelAssembler;
import com.vepilef.food.api.v1.model.FormaPagamentoModel;
import com.vepilef.food.api.v1.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.core.security.FoodSecurity;
import com.vepilef.food.domain.model.Restaurante;
import com.vepilef.food.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/restaurantes/{idRestaurante}/formas-pagamento",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private FoodLinks foodLinks;

    @Autowired
    private FoodSecurity foodSecurity;

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping
    public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long idRestaurante) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(idRestaurante);
        CollectionModel<FormaPagamentoModel> formaPagamentoModels = formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(foodLinks.linkToRestauranteFormasPagamento(idRestaurante));

        if (foodSecurity.podeGerenciarFuncionamentoRestaurantes(idRestaurante)) {
            formaPagamentoModels.add(foodLinks.linkToRestauranteFormaPagamentoAssociacao(idRestaurante, "associar"));

            formaPagamentoModels.getContent().forEach(formaPagamentoModel -> {
                formaPagamentoModel.add(foodLinks.linkToRestauranteFormaPagamentoDesassociacao(
                        idRestaurante, formaPagamentoModel.getId(),
                        "desassociar"
                ));
            });
        }

        return formaPagamentoModels;
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @DeleteMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long idRestaurante, @PathVariable Long idFormaPagamento) {
        cadastroRestauranteService.desassociarFormaPagamento(idRestaurante, idFormaPagamento);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long idRestaurante, @PathVariable Long idFormaPagamento) {
        cadastroRestauranteService.associarFormaPagamento(idRestaurante, idFormaPagamento);
        return ResponseEntity.noContent().build();
    }

}
