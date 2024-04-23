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
@Table(name="tb_item_pedido")
public class ItemPedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item_pedido")
	private Long id;
	
	private BigDecimal precoUnitario;
	
	private BigDecimal precoTotal;
	
	private Integer quantidade;
	
	private String observacao;
	
	@ManyToOne
	@JoinColumn(name="id_produto", nullable = false)
	private Produto produto;
	
	@ManyToOne
	@JoinColumn(name="id_pedido", nullable = false)
	private Pedido pedido;
	
	public void calcularPrecoTotal() {
	    BigDecimal precoUnitario = this.getPrecoUnitario();
	    Integer quantidade = this.getQuantidade();

	    if (precoUnitario == null) {
	        precoUnitario = BigDecimal.ZERO;
	    }

	    if (quantidade == null) {
	        quantidade = 0;
	    }

	    this.setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));
	}
	
}
