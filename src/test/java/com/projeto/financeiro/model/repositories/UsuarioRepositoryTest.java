package com.projeto.financeiro.model.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.projeto.financeiro.model.entity.Usuario;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarAExistenciaDeEmail() {
		// Cenario

		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		// Execução
		boolean result = repository.existsByEmail("email@email.com");

		// Verificacao
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void deveVerificarFalsoSeNaoAcharEmail() {

		boolean result = repository.existsByEmail("email3@email.com");

		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void devePersistirUmUsuario() {
		Usuario usuario = criarUsuario();

		Usuario usuarioSalvo = repository.save(usuario);

		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();

	}

	@Test
	public void deveBuscarUsuarioPorEmail() {

		Usuario usuario = criarUsuario();

		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("email@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveBuscarUsuarioPorEmailEDarErroAoNaoAchar() {

		Optional<Usuario> result = repository.findByEmail("email@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}

	private Usuario criarUsuario() {
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").senha("123").build();
		return usuario;
	}
}
