package br.com.alura.flix.app.seguranca.models;

import lombok.Getter;

@Getter
public class CadastrarUsuarioRequest {

	private String usuario;
	private String senha;
}
