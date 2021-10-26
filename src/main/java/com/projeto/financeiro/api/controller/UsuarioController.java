package com.projeto.financeiro.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.financeiro.api.dto.UsuarioDto;
import com.projeto.financeiro.exception.AutenticacaoException;
import com.projeto.financeiro.exception.RegraNegocioException;
import com.projeto.financeiro.model.entity.Usuario;
import com.projeto.financeiro.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@SuppressWarnings("rawtypes")
public class UsuarioController {

	private UsuarioService service;

	public UsuarioController(UsuarioService service) {
		this.service = service;
	}

	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDto dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());

			return ResponseEntity.ok(usuarioAutenticado);
			
		} catch (AutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDto dto) {

		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();

		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}
