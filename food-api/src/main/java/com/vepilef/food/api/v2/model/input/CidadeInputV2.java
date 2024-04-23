package com.vepilef.food.api.v2.model.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@ApiModel("CidadeInput")
public class CidadeInputV2 {

    @ApiModelProperty(example = "Fortaleza", required = true)
    @NotBlank
    private String nomeCidade;

    @ApiModelProperty(example = "1", required = true)
    @NotNull
    private Long idEstado;

}
