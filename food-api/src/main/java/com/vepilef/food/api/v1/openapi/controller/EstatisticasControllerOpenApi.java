package com.vepilef.food.api.v1.openapi.controller;

import com.vepilef.food.api.v1.controller.EstatisticasController;
import com.vepilef.food.domain.filter.VendaDiariaFilter;
import com.vepilef.food.domain.model.dto.VendaDiaria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Estatísticas")
public interface EstatisticasControllerOpenApi {

    @ApiOperation(value = "Estatísticas", hidden = true)
    EstatisticasController.EstatisticasModel estatisticas();


    @ApiOperation("Consulta estatísticas de vendas diárias")
    @EstatisticasVendasHeaders
    List<VendaDiaria> consultarVendasDiarias(
            VendaDiariaFilter filtro,
            @ApiParam(value = "Deslocamento de horário a ser considerado na consulta em relação ao UTC",
                    defaultValue = "+00:00")
                    String timeOffset);

    
    ResponseEntity<byte[]> consultarVendasDiariasPdf(
            VendaDiariaFilter filtro,
            String timeOffset);

}
