package br.com.alura.flix.app.videos.models;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;

@Getter
public class CadastrarVideoRequest {

	@NotBlank(message = "O título deve ser preenchido")
	private String titulo;
	
	@NotBlank(message = "A descrição deve ser preenchida")
	private String descricao;
	
	@URL(message = "O valor preenchido na URL é invalido")
	@NotBlank(message = "O link do vídeo deve ser preenchido")
	private String url;
}
