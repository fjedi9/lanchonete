package com.vepilef.food.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vepilef.food.domain.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{

}
