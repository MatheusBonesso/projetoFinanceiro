package com.projeto.financeiro.model.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.projeto.financeiro.model.entity.Lancamento;
import com.projeto.financeiro.model.enums.TipoLancamento;

@EnableJpaRepositories
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	@Query(value = " select sum(l.valor) from Lancamento l join l.usuario u where u.id = :idUsuario and l.tipoLancamento = :tipo group by u ")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);
}
