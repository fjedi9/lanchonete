package com.vepilef.food.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_produto")
public class Produto {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produto", nullable = false)
	private Long id;

	@Column(name = "nm_produto", nullable = false)
	private String nome;

	@Column(name = "ds_produto", nullable = false)
	private String descricao;

	@Column(nullable = false)
	private BigDecimal preco;

	@Column(nullable = false)
	private Boolean ativo;

	@ManyToOne
	@JoinColumn(name = "id_restaurante", nullable = false)
	private Restaurante restaurante;

}
