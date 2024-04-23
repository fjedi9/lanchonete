package com.vepilef.food.api.v1.assembler;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vepilef.food.api.v1.model.input.CidadeInput;
import com.vepilef.food.domain.model.Cidade;
import com.vepilef.food.domain.model.Estado;

@Component
public class CidadeInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Cidade toDomainObject(@Valid CidadeInput cidadeInput) {
		return modelMapper.map(cidadeInput, Cidade.class);
	}

	public void copyToDomainObject(@Valid CidadeInput cidadeInput, Cidade cidade) {
		cidade.setEstado(new Estado());
		modelMapper.map(cidadeInput, cidade);
	}
	
}
