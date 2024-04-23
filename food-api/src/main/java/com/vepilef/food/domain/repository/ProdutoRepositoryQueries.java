package com.vepilef.food.domain.repository;

import com.vepilef.food.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);

	void delete(FotoProduto foto);
	
}