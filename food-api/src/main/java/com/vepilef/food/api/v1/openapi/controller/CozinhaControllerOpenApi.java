package com.vepilef.food.api.v1.openapi.controller;

import com.vepilef.food.api.exceptionhandler.Problem;
import com.vepilef.food.api.v1.model.CozinhaModel;
import com.vepilef.food.api.v1.model.input.CozinhaInput;
import io.swagger.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@Api(tags = "Cozinhas")
public interface CozinhaControllerOpenApi {

    @ApiOperation("Lista as cozinhas")
    PagedModel<CozinhaModel> listar(Pageable pageable);


    @ApiOperation("Busca uma cozinha por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID da cozinha inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Cozinha não encontrada", response = Problem.class)
    })
    CozinhaModel buscar(@ApiParam(value = "ID de uma cozinha", example = "1")
                                       Long idCozinha);


    @ApiOperation("Cadastra uma cozinha")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cozinha cadastrado")
    })
    CozinhaModel salvar(@ApiParam(name = "corpo", value = "Representação de uma cozinha")
                                       CozinhaInput grupoInput);


    @ApiOperation("Atualiza uma cozinha por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cozinha atualizada"),
            @ApiResponse(code = 404, message = "Cozinha não encontrada", response = Problem.class)
    })
    CozinhaModel atualizar(@ApiParam(value = "ID de uma cozinha", example = "1")
                                          Long id,
                                  @ApiParam(name = "corpo", value = "Representação de uma cozinha com os novos dados")
                                          CozinhaInput grupoInput);


    @ApiOperation("Exclui uma cozinha por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cozinha excluído"),
            @ApiResponse(code = 404, message = "Cozinha não encontrada", response = Problem.class)
    })
    void remover(@ApiParam(value = "ID de uma cozinha", example = "1")
                                Long idCozinha);

}

