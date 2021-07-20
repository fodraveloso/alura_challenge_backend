package br.com.alura.flix.app.models;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;

@Getter
public class AtualizarVideoRequest {

	private String titulo;
	
	private String descricao;
	
	@URL(message = "O valor preenchido na URL é invalido")
	private String url;
}
