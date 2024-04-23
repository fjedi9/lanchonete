package com.vepilef.food.domain.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_grupo")
public class Grupo {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_grupo")
	private Long id;
	
	@Column(name = "nm_grupo", nullable = false)
	private String nome;
	
	@ManyToMany
	@JoinTable(name = "tb_grupo_permissao", joinColumns = @JoinColumn(name = "id_grupo"),
			inverseJoinColumns = @JoinColumn(name = "id_permissao"))
	private Set<Permissao> permissoes = new HashSet<>();

	public boolean removerPermissao(Permissao permissao) {
	    return getPermissoes().remove(permissao);
	}

	public boolean adicionarPermissao(Permissao permissao) {
	    return getPermissoes().add(permissao);
	}
	
}