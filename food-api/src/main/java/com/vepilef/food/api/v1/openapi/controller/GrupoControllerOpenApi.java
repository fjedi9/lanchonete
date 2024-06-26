package com.vepilef.food.api.v1.openapi.controller;

import com.vepilef.food.api.exceptionhandler.Problem;
import com.vepilef.food.api.v1.model.GrupoModel;
import com.vepilef.food.api.v1.model.input.GrupoInput;
import io.swagger.annotations.*;
import org.springframework.hateoas.CollectionModel;

@Api(tags = "Grupos")
public interface GrupoControllerOpenApi {

    @ApiOperation("Lista os grupos")
    CollectionModel<GrupoModel> listar();

    @ApiOperation("Busca um grupo por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID do grupo inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    GrupoModel buscar(@ApiParam(value = "ID de um grupo", example = "1", required = true)
                                     Long idGrupo);

    @ApiOperation("Cadastra um grupo")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Grupo cadastrado")
    })
    GrupoModel salvar(@ApiParam(name = "corpo", value = "Representação de um grupo", required = true)
                                     GrupoInput grupoInput);


    @ApiOperation("Atualiza um grupo por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Grupo atualizado"),
            @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    GrupoModel atualizar(@ApiParam(value = "ID de um grupo", example = "1", required = true) Long id,
                                @ApiParam(name = "corpo", value = "Representação de um grupo com os novos dados",
                                        required = true)
                                        GrupoInput grupoInput);

    @ApiOperation("Exclui um grupo por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Grupo excluído"),
            @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    void remover(@ApiParam(value = "ID de um grupo", example = "1", required = true) Long idGrupo);

}

