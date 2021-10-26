package com.projeto.financeiro.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.financeiro.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
	
}
