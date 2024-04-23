package com.vepilef.food.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vepilef.food.domain.model.FormaPagamento;

import java.time.OffsetDateTime;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

    @Query("select max(dataAtualizacao) from FormaPagamento")
    OffsetDateTime getDataUltimaAtualizacao();

    @Query("select max(dataAtualizacao) from FormaPagamento where id = :idFormaPagamento")
    OffsetDateTime getDataUltimaAtualizacaoById(Long idFormaPagamento);

}
