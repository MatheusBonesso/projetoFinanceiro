package com.projeto.financeiro.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.financeiro.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
