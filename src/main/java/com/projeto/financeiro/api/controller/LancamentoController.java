package com.projeto.financeiro.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.financeiro.api.dto.AtualizaStatusDto;
import com.projeto.financeiro.api.dto.LancamentoDto;
import com.projeto.financeiro.exception.RegraNegocioException;
import com.projeto.financeiro.model.entity.Lancamento;
import com.projeto.financeiro.model.entity.Usuario;
import com.projeto.financeiro.model.enums.StatusLancamento;
import com.projeto.financeiro.model.enums.TipoLancamento;
import com.projeto.financeiro.services.LancamentoService;
import com.projeto.financeiro.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentoService service;

	private final UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam("usuario") Long idUsuario) {

		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);

		Optional<Usuario> usuario = usuarioService.obterId(idUsuario);

		if (usuario.isEmpty())
			return ResponseEntity.badRequest().body("Usuario não encontrado");
		else
			lancamentoFiltro.setUsuario(usuario.get());

		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);

		return ResponseEntity.ok(lancamentos);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDto dto) {
		try {
			Lancamento lancamento = converter(dto);
			lancamento = service.salvar(lancamento);

			return new ResponseEntity(lancamento, HttpStatus.OK);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@RequestBody AtualizaStatusDto dto,@PathVariable("id") Long id) {
		
		
		return service.obterPorId(id).map(entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			if(statusSelecionado == null)
				return ResponseEntity.badRequest().body("Status invalido");
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				
				return ResponseEntity.ok(entity);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado", HttpStatus.BAD_REQUEST));
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(id);
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}

	private Lancamento converter(LancamentoDto dto) {
		Lancamento lancamento = new Lancamento();

		Usuario usuario = usuarioService.obterId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario nao encontrado."));

		lancamento.setId(dto.getId());
		lancamento.setUsuario(usuario);
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		if (dto.getTipo() != null)
			lancamento.setTipoLancamento(TipoLancamento.valueOf(dto.getTipo()));

		if (dto.getStatus() != null)
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

		return lancamento;
	}

}
