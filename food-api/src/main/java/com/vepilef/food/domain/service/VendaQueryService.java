package com.vepilef.food.domain.service;

import com.vepilef.food.domain.filter.VendaDiariaFilter;
import com.vepilef.food.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter vendaDiariaFilter, String timeOffset);

}
