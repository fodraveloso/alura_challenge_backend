package br.com.alura.flix.app.videos.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErroResponse {

	private final int codigo;
	private List<String> erros = new ArrayList<>();
	
	public ErroResponse erro(String mensagem) {
		
		this.erros.add(mensagem);
		return this;
	}
}