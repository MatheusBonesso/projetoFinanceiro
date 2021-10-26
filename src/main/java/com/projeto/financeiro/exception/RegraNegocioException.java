package com.projeto.financeiro.exception;

@SuppressWarnings("serial")
public class RegraNegocioException extends RuntimeException {

	public RegraNegocioException(String error) {
		super(error);
	}
}
