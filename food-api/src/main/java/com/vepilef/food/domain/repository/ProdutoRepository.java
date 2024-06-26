package com.vepilef.food.domain.repository;

import java.util.List;
import java.util.Optional;

import com.vepilef.food.domain.model.FotoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vepilef.food.domain.model.Produto;
import com.vepilef.food.domain.model.Restaurante;

public interface ProdutoRepository
		extends JpaRepository<Produto, Long>, ProdutoRepositoryQueries {

	@Query("from Produto where restaurante.id = :idRestaurante and id = :idProduto")
	Optional<Produto> findById(@Param("idRestaurante") Long idRestaurante, @Param("idProduto") Long idProduto);
	
	List<Produto> findByRestaurante(Restaurante restaurante);
	
	@Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
	List<Produto> findAtivosByRestaurante(Restaurante restaurante);

	@Query("Select f from FotoProduto f join f.produto p " +
			"where f.produto.id = :idProduto " +
			"and p.restaurante.id = :idRestaurante")
	Optional<FotoProduto> findFotoById(Long idRestaurante, Long idProduto);
	
}
