package com.projeto.financeiro.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.projeto.financeiro.model.enums.StatusLancamento;
import com.projeto.financeiro.model.enums.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lancamento", schema = "financas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "mes")
	private Integer mes;
	
	@Column(name = "ano")
	private Integer ano;

	@Column(name = "valor")
	private BigDecimal valor;

	@Column(name = "tipo")
	private TipoLancamento tipoLancamento;

	@Column(name = "status")
	private StatusLancamento status;
	
	@Column(name = "id_usuario")
	private Usuario usuario;
}
