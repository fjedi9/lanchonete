package com.vepilef.food.domain.service;

import com.vepilef.food.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

    byte[] emitirVendasDiarias(VendaDiariaFilter filter, String timeOffset);

}
