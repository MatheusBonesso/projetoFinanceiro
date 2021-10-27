package com.projeto.financeiro.services;

import java.util.Optional;

import com.projeto.financeiro.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> obterId(Long id);
}
