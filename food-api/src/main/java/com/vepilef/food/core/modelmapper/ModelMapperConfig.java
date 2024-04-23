package com.vepilef.food.core.modelmapper;

import com.vepilef.food.api.v2.model.input.CidadeInputV2;
import com.vepilef.food.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vepilef.food.api.v1.model.EnderecoModel;
import com.vepilef.food.api.v1.model.RestauranteModel;
import com.vepilef.food.api.v1.model.input.ItemPedidoInput;
import com.vepilef.food.domain.model.Endereco;
import com.vepilef.food.domain.model.ItemPedido;
import com.vepilef.food.domain.model.Restaurante;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();

		modelMapper.createTypeMap(CidadeInputV2.class, Cidade.class)
		.addMappings(mapper -> mapper.skip(Cidade::setId));

		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
				.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete) // Utilizando referências de métodos
				.addMapping(restSrc -> restSrc.getEndereco().getCidade().getEstado().getNome(),
						(restDest, val) -> restDest.getEndereco().getCidade().setEstado((String) val));

		var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(
				Endereco.class, EnderecoModel.class);

		enderecoToEnderecoModelTypeMap.<String>addMapping(
				enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
				(enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));

		modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
	    	.addMappings(mapper -> mapper.skip(ItemPedido::setId)); // Ao fazer o mapeameto ignore o setId
		
		return modelMapper;
	}
	
}
