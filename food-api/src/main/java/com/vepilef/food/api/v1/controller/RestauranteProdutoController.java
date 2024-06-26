package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.FoodLinks;
import com.vepilef.food.api.v1.assembler.ProdutoInputDisassembler;
import com.vepilef.food.api.v1.assembler.ProdutoModelAssembler;
import com.vepilef.food.api.v1.model.ProdutoModel;
import com.vepilef.food.api.v1.model.input.ProdutoInput;
import com.vepilef.food.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.domain.model.Produto;
import com.vepilef.food.domain.model.Restaurante;
import com.vepilef.food.domain.repository.ProdutoRepository;
import com.vepilef.food.domain.service.CadastroProdutoService;
import com.vepilef.food.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/restaurantes/{idRestaurante}/produtos",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private CadastroProdutoService cadastroProdutoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoModelAssembler produtoModelAssembler;

    @Autowired
    private ProdutoInputDisassembler produtoInputDisassembler;

    @Autowired
    private FoodLinks foodLinks;

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping
    public CollectionModel<ProdutoModel> listar(@PathVariable Long idRestaurante,
                                                @RequestParam(required = false) Boolean incluirInativos) {

        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(idRestaurante);
        List<Produto> todosProdutos = null;

        if (incluirInativos) {
            todosProdutos = produtoRepository.findByRestaurante(restaurante);
        } else {
            todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
        }
        return produtoModelAssembler.toCollectionModel(todosProdutos)
                .add(foodLinks.linkToProdutos(idRestaurante));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping("/{idProduto}")
    public ProdutoModel buscar(@PathVariable Long idRestaurante, @PathVariable Long idProduto) {
        Produto produto = cadastroProdutoService.buscarOuFalhar(idRestaurante, idProduto);
        return produtoModelAssembler.toModel(produto);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long idRestaurante, @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(idRestaurante);

        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);

        produto = cadastroProdutoService.salvar(produto);

        return produtoModelAssembler.toModel(produto);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{idProduto}")
    public ProdutoModel atualizar(@PathVariable Long idRestaurante, @PathVariable Long idProduto,
                                  @RequestBody @Valid ProdutoInput produtoInput) {

        Produto produtoAtual = cadastroProdutoService.buscarOuFalhar(idRestaurante, idProduto);
        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        produtoAtual = cadastroProdutoService.salvar(produtoAtual);

        return produtoModelAssembler.toModel(produtoAtual);
    }

}
