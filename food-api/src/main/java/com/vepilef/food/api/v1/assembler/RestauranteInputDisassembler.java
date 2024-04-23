package com.vepilef.food.api.v1.assembler;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vepilef.food.api.v1.model.input.RestauranteInput;
import com.vepilef.food.domain.model.Cidade;
import com.vepilef.food.domain.model.Cozinha;
import com.vepilef.food.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toDomainObject(@Valid RestauranteInput restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);
	}

	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		restaurante.setCozinha(new Cozinha());
		
		if (restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}
	
}
