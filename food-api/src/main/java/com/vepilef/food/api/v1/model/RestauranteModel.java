package com.vepilef.food.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Relation(collectionRelation = "restaurantes")
@Setter
@Getter
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

	@ApiModelProperty(example = "1")
	private Long id;

	@ApiModelProperty(example = "Thai Gourmet")
	private String nome;

	@ApiModelProperty(example = "10.00")
	private BigDecimal precoFrete;

	private CozinhaModel cozinha;
	
	private Boolean ativo;
	
	private EnderecoModel endereco;
	
	private Boolean aberto;
	
}
