package com.projeto.financeiro.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.financeiro.exception.AutenticacaoException;
import com.projeto.financeiro.exception.RegraNegocioException;
import com.projeto.financeiro.model.entity.Usuario;
import com.projeto.financeiro.model.repositories.UsuarioRepository;
import com.projeto.financeiro.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);

		if (!usuario.isPresent())
			throw new AutenticacaoException("Usuario nao encontrado.");

		if (!usuario.get().getSenha().equals(senha))
			throw new AutenticacaoException("Senha Invalida.");

		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {

		validarEmail(usuario.getEmail());

		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);

		if (existe) {
			throw new RegraNegocioException("Ja existe um usuario com este email.");
		}
	}

	@Override
	public Optional<Usuario> obterId(Long id) {
		return repository.findById(id);
	}

}
