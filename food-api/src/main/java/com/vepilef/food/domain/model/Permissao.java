package com.vepilef.food.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_permissao")
public class Permissao {

	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_permissao")
    private Long id;
    
    @Column(name = "nm_permissao", nullable = false)
    private String nome;
    
    @Column(name = "ds_permissao", nullable = false)
    private String descricao;

}
