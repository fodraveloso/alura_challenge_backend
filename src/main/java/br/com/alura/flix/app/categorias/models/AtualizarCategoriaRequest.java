package br.com.alura.flix.app.categorias.models;

import br.com.alura.flix.app.categorias.validations.AtualizarCategoriaValidation;
import lombok.Getter;

@Getter
@AtualizarCategoriaValidation(message = "Os dados informados para atualização são inválidos")
public class AtualizarCategoriaRequest {

	private String titulo;
	private String cor;
}