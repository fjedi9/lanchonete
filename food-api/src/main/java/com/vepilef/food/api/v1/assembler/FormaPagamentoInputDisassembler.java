package com.vepilef.food.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vepilef.food.api.v1.model.input.FormaPagamentoInput;
import com.vepilef.food.domain.model.FormaPagamento;

@Component
public class FormaPagamentoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public FormaPagamento toDomainObject(FormaPagamentoInput formaPagamentoInput) {
        return modelMapper.map(formaPagamentoInput, FormaPagamento.class);
    }
    
    public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
        modelMapper.map(formaPagamentoInput, formaPagamento);
    }
    
}