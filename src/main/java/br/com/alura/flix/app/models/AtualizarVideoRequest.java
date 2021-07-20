package br.com.alura.flix.app.models;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class AtualizarVideoRequest {

	@NotBlank(message = "O título deve ser preenchido")
	private String titulo;
	
	@NotBlank(message = "A descrição deve ser preenchida")
	private String descricao;
	
	@NotBlank(message = "O link do vídeo deve ser preenchido")
	private String url;
}
