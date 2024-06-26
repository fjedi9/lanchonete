package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.FoodLinks;
import com.vepilef.food.api.v1.assembler.PermissaoModelAssembler;
import com.vepilef.food.api.v1.model.PermissaoModel;
import com.vepilef.food.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.core.security.FoodSecurity;
import com.vepilef.food.domain.model.Grupo;
import com.vepilef.food.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/grupos/{idGrupo}/permissoes",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

    @Autowired
    private CadastroGrupoService cadastroGrupo;

    @Autowired
    private PermissaoModelAssembler permissaoModelAssembler;

    @Autowired
    private FoodLinks foodLinks;

    @Autowired
    private FoodSecurity foodSecurity;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @Override
    @GetMapping
    public CollectionModel<PermissaoModel> listar(@PathVariable Long idGrupo) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(idGrupo);
        CollectionModel<PermissaoModel> permissoesModel = permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
                .removeLinks()
                .add(foodLinks.linkToGrupoPermissoes(idGrupo));

        if (foodSecurity.podeEditarUsuariosGruposPermissoes()) {
            permissoesModel.add(foodLinks.linkToGrupoPermissaoAssociacao(idGrupo, "associar"));

            permissoesModel.getContent().forEach(permissaoModel -> {
                permissaoModel.add(foodLinks.linkToGrupoPermissaoDesassociacao(
                        idGrupo, permissaoModel.getId(), "desassociar"));
            });
        }

        return permissoesModel;
    }

    @Override
    @DeleteMapping("/{idPermissao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long idGrupo, @PathVariable Long idPermissao) {
        cadastroGrupo.desassociarPermissao(idGrupo, idPermissao);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{idPermissao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long idGrupo, @PathVariable Long idPermissao) {
        cadastroGrupo.associarPermissao(idGrupo, idPermissao);
        return ResponseEntity.noContent().build();
    }

}
