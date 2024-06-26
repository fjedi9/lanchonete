package com.vepilef.food.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.vepilef.food.core.validation.ValorZeroIncluiDescricao;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_restaurante")
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_restaurante")
	private Long id;

	@Column(name = "nm_restaurante", length = 100, nullable = false)
	private String nome;

	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	@ManyToOne // (fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cozinha", nullable = false)
	private Cozinha cozinha;

	@Embedded // Incorporação
	private Endereco endereco;
	
	@Column
	private Boolean ativo = Boolean.TRUE;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime") // datetime para remover precisão de milisegundos q seria CreationTimestamp(6)
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;

	@ManyToMany
	@JoinTable(name = "tb_restaurante_forma_pagamento", 
		joinColumns = @JoinColumn(name = "id_restaurante"), // Indica a coluna na tabela local (Restaurante) que será o nome da coluna relacionada
		inverseJoinColumns = @JoinColumn(name = "id_forma_pagamento"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>(); 
	// Set é um conjunto que não aceita elementos duplicados, evitando erro de restaurante receber forma de pagamento já associada

	@ManyToMany
	@JoinTable(name = "tb_restaurante_usuario_responsavel",
			joinColumns = @JoinColumn(name = "id_restaurante"),
			inverseJoinColumns = @JoinColumn(name = "id_usuario"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();

	@Column
	private Boolean aberto = Boolean.FALSE;

	public void abrir() {
	    setAberto(true);
	}

	public void fechar() {
	    setAberto(false);
	} 

	public void ativar() {
		setAtivo(true);
	}
	
	public void inativar() {
		setAtivo(false);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().remove(formaPagamento);
	}

	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().add(formaPagamento);
	}
	
	public boolean desassociarResponsavel(Usuario usuario) {
		return getResponsaveis().remove(usuario);
	}
	
	public boolean associarResponsavel(Usuario usuario) {
		return getResponsaveis().add(usuario);
	}

	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().contains(formaPagamento);
	}
	
	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !aceitaFormaPagamento(formaPagamento);
	}

	// Utilizados para adicção condicional de links do HATEOAS
	public boolean isAberto() {
		return this.aberto;
	}

	public boolean isFechado() {
		return !isAberto();
	}

	public boolean isInativo() {
		return !isAtivo();
	}

	public boolean isAtivo() {
		return this.ativo;
	}

	public boolean aberturaPermitida() {
		return isAtivo() && isFechado();
	}

	public boolean ativacaoPermitida() {
		return isInativo();
	}

	public boolean inativacaoPermitida() {
		return isAtivo();
	}

	public boolean fechamentoPermitido() {
		return isAberto();
	}

}
