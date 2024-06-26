package com.vepilef.food.api.v1.assembler;

import com.vepilef.food.api.v1.FoodLinks;
import com.vepilef.food.api.v1.controller.RestauranteProdutoController;
import com.vepilef.food.api.v1.model.ProdutoModel;
import com.vepilef.food.core.security.FoodSecurity;
import com.vepilef.food.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler
        extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FoodLinks foodLinks;

    @Autowired
    private FoodSecurity foodSecurity;

    public ProdutoModelAssembler() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    public ProdutoModel toModel(Produto produto) {
        // id do restaurante pois é parâmetro em RequestMapping do Controller
        ProdutoModel produtoModel = createModelWithId(produto.getId(), produto,
                produto.getRestaurante().getId());

        modelMapper.map(produto, produtoModel);

        if (foodSecurity.podeConsultarRestaurantes()) {
            produtoModel.add(foodLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));
            produtoModel.add(foodLinks.linkToFotoProduto(produto.getRestaurante().getId(),
                    produto.getId(), "foto"));
        }
        return produtoModel;
    }

}
