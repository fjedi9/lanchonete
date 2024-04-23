package com.vepilef.food.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.vepilef.food.core.validation.Groups;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JsonRootName("cozinha") // Altera o nome do objeto no xml de "Cozinha" para "cozinha"
@Getter
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_cozinha")
public class Cozinha {

	@NotNull(groups = Groups.CozinhaId.class)
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cozinha")
	private Long id;

	@Column(name = "nm_cozinha", length = 50, nullable = false)
	private String nome;
	
	@OneToMany(mappedBy = "cozinha") // Mapeado pelo atributo 'cozinha' na entidade Restaurante
	private List<Restaurante> restaurantes = new ArrayList<>();

}
