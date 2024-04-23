package com.vepilef.food.infrastructure.repository;

import com.vepilef.food.domain.model.FotoProduto;
import com.vepilef.food.domain.repository.ProdutoRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto foto) {
        manager.persist(foto);
        return foto;
    }

    @Override
    public void delete(FotoProduto foto) {
        manager.remove(foto);
    }

}
