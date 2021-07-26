package br.com.alura.flix.app.categorias.models;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CadastrarCategoriaRequest {

	@NotBlank(message = "O título deve ser preenchido")
	private String titulo;
	
	@NotBlank(message = "A cor deve ser preenchida")
	private String cor;
}
