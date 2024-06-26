package com.vepilef.food.api.v1.openapi.controller;

import com.vepilef.food.api.exceptionhandler.Problem;
import com.vepilef.food.api.v1.model.ProdutoModel;
import com.vepilef.food.api.v1.model.input.ProdutoInput;
import io.swagger.annotations.*;
import org.springframework.hateoas.CollectionModel;

@Api(tags = "Produtos")
public interface RestauranteProdutoControllerOpenApi {

    @ApiOperation("Lista os produtos de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID do restaurante inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    CollectionModel<ProdutoModel> listar(
            @ApiParam(value = "ID do restaurante", example = "1", required = true)
                    Long idRestaurante,
            @ApiParam(value = "Indica se deve ou não incluir produtos inativos no resultado da listagem",
                    example = "false", defaultValue = "false")
                    Boolean incluirInativos);


    @ApiOperation("Busca um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID do restaurante ou produto inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class)
    })
    ProdutoModel buscar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long idRestaurante,
            @ApiParam(value = "ID de um produto", example = "1", required = true)
                    Long idProduto);


    @ApiOperation("Cadastra um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class),
            @ApiResponse(code = 201, message = "Produto cadastrado")
    })
    ProdutoModel adicionar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long idRestaurante,
            @ApiParam(name = "corpo", value = "Representação de um produto")
                    ProdutoInput produtoInput);


    @ApiOperation("Atualiza um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class),
            @ApiResponse(code = 201, message = "Produto atualizado")
    })
    ProdutoModel atualizar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long idRestaurante,
            @ApiParam(value = "ID de um produto", example = "1", required = true)
                    Long idProduto,
            @ApiParam(name = "corpo", value = "Representação de um produto com os novos dados")
                    ProdutoInput produtoInput);

}
