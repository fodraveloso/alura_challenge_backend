package br.com.alura.flix.app.models;

import org.hibernate.validator.constraints.URL;

import br.com.alura.flix.app.validations.AtualizarVideoValidation;
import lombok.Getter;

@Getter
@AtualizarVideoValidation(message = "Os dados informados para atualização são inválidos")
public class AtualizarVideoRequest {

	private String titulo;
	
	private String descricao;
	
	@URL(message = "O valor preenchido na URL é invalido")
	private String url;
}
