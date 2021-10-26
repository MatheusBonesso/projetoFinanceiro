package com.projeto.financeiro.services;

import java.util.List;

import com.projeto.financeiro.model.entity.Lancamento;
import com.projeto.financeiro.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lancamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);

}
