package com.projeto.financeiro.service.test;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import com.projeto.financeiro.exception.AutenticacaoException;
import com.projeto.financeiro.exception.RegraNegocioException;
import com.projeto.financeiro.model.entity.Usuario;
import com.projeto.financeiro.model.repositories.UsuarioRepository;
import com.projeto.financeiro.services.impl.UsuarioServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		String email = "email@email.com";
		String senha = "123";

		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

		Usuario resultado = service.autenticar(email, senha);

		Assertions.assertThat(resultado).isNotNull();
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuario() {
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> {
			service.autenticar("email@email.com", "123");
		}).isInstanceOf(AutenticacaoException.class).hasMessage("Usuario nao encontrado.");
	}

	public void deveSalvarUsuario() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());

		Usuario usuario = Usuario.builder().nome("nome").email("email@eamil.com").senha("senha").id(1l).build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario resultado = service.salvarUsuario(new Usuario());

		Assertions.assertThat(resultado).isNotNull();
	}

	@Test
	public void lancarErroComSenhaInvalida() {
		String senha = "123";
		String email = "email@email";
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		Assertions.assertThatThrownBy(() -> {
			service.autenticar(email, "");
		}).isInstanceOf(AutenticacaoException.class).hasMessage("Senha Invalida.");
	}

	@Test
	public void deveValidarEmail() {

		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		service.validarEmail("email@email.com");
	}

	@Test
	public void deveLancarExcecao() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		Assertions.assertThatThrownBy(() -> {
			service.validarEmail("email@email.com");
		}).isInstanceOf(RegraNegocioException.class).hasMessage("Ja existe um usuario com este email.");
	}

}
