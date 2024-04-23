package com.vepilef.food.api.v1.openapi.controller;

import com.vepilef.food.api.exceptionhandler.Problem;
import com.vepilef.food.api.v1.model.PedidoModel;
import com.vepilef.food.api.v1.model.PedidoResumoModel;
import com.vepilef.food.api.v1.model.input.PedidoInput;
import com.vepilef.food.domain.filter.PedidoFilter;
import io.swagger.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.converter.json.MappingJacksonValue;

@Api(tags = "Pedidos")
public interface PedidoControllerOpenApi {

    @ApiOperation("Pesquisa os pedidos")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string")
    })
    PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable);


    @ApiOperation("Pesquisa pedidos, retorna apenas propriedades desejadas")
    MappingJacksonValue listarPedidosFilter(
            @ApiParam(value = "campos", example = "codigo,valorTotal") String campos);


    @ApiOperation("Busca um pedido por Código")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    PedidoModel buscar(@ApiParam(value = "Código de um pedido",
            example = "335c0344-f4c5-4824-ad8a-944c9e6bdf43") String codigoPedido);


    @ApiOperation("Cadastra um pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido registrado")
    })
    PedidoModel adicionar(@ApiParam(name = "corpo", value = "Representação de um pedido")
                                      PedidoInput pedidoInput);

}

