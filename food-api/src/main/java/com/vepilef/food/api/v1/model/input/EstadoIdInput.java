package com.vepilef.food.api.v1.model.input;

import jakarta.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoIdInput {

	@ApiModelProperty(example = "1", required = true)
	@NotNull
	private Long id;

}
