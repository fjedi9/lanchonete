package com.vepilef.food.api.v1.model.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInput {

    @ApiModelProperty(example = "38400-000", required = true)
    @NotBlank
    private String cep;

    @ApiModelProperty(example = "Rua Floriano Peixoto", required = true)
    @NotBlank
    private String logradouro;

    @ApiModelProperty(example = "\"1500\"", required = true)
    @NotBlank
    private String numero;

    @ApiModelProperty(example = "Apto 901")
    private String complemento;

    @ApiModelProperty(example = "Centro", required = true)
    @NotBlank
    private String bairro;

    @Valid
    @NotNull
    private CidadeIdInput cidade;

}
