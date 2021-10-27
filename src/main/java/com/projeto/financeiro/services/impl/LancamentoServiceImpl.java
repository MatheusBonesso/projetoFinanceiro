package com.projeto.financeiro.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.financeiro.exception.RegraNegocioException;
import com.projeto.financeiro.model.entity.Lancamento;
import com.projeto.financeiro.model.enums.StatusLancamento;
import com.projeto.financeiro.model.enums.TipoLancamento;
import com.projeto.financeiro.model.repositories.LancamentoRepository;
import com.projeto.financeiro.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;

	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.nonNull(lancamento);

		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);

	}

	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals(""))
			throw new RegraNegocioException("Informe descricao valida.");
		if(lancamento.getMes()== null || lancamento.getMes() < 1 || lancamento.getMes() > 12)
			throw new RegraNegocioException("Informe mes valido.");
		if(lancamento.getAno()== null || lancamento.getAno().toString().length() != 4)
			throw new RegraNegocioException("Informe ano valido.");
		if(lancamento.getUsuario()== null || lancamento.getUsuario().getId() == null)
			throw new RegraNegocioException("Informe usuario.");
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1)
			throw new RegraNegocioException("Informe valor valido.");
		if(lancamento.getTipoLancamento() == null)
			throw new RegraNegocioException("Informe tipo de lancamento.");
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesa = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if(receitas == null)
			receitas = BigDecimal.ZERO;
		if(despesa == null)
			despesa = BigDecimal.ZERO;
		
		
		return receitas.subtract(despesa);
	}

}
