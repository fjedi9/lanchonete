package com.vepilef.food.api.v1.assembler;

import com.vepilef.food.api.v1.FoodLinks;
import com.vepilef.food.api.v1.controller.RestauranteController;
import com.vepilef.food.api.v1.model.RestauranteModel;
import com.vepilef.food.core.security.FoodSecurity;
import com.vepilef.food.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FoodLinks foodLinks;

    @Autowired
    private FoodSecurity foodSecurity;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);

        if (foodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(foodLinks.linkToRestaurantes("restaurantes"));
        }

        if (foodSecurity.podeGerenciarCadastroRestaurantes()) {
            if (restaurante.ativacaoPermitida()) {
                restauranteModel.add(foodLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
            }

            if (restaurante.inativacaoPermitida()) {
                restauranteModel.add(foodLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
            }
        }

        if (foodSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {

            if (restaurante.aberturaPermitida()) {
                restauranteModel.add(foodLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
            }

            if (restaurante.fechamentoPermitido()) {
                restauranteModel.add(foodLinks.linkToRestauranteFechamento(restaurante.getId(),
                        "fechar"));
            }
        }

        if (foodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(foodLinks.linkToProdutos(restaurante.getId(), "produtos"));
        }

        if (foodSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    foodLinks.linkToCozinha(restaurante.getCozinha().getId()));
        }

        if (foodSecurity.podeConsultarCidades()) {
            if (restauranteModel.getEndereco() != null && restauranteModel.getEndereco().getCidade() != null) {
                restauranteModel.getEndereco().getCidade().add(
                        foodLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
            }
        }

        if (foodSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(foodLinks.linkToRestauranteFormasPagamento(restaurante.getId(),
                    "formas-pagamento"));
        }

        if (foodSecurity.podeGerenciarCadastroRestaurantes()) {
            restauranteModel.add(foodLinks.linkToRestauranteResponsaveis(restaurante.getId(),
                    "responsaveis"));
        }
        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteModel> collectionModel = super.toCollectionModel(entities);

        if (foodSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(foodLinks.linkToRestaurantes());
        }

        return collectionModel;
    }

}
