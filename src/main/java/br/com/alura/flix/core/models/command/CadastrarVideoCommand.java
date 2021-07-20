package br.com.alura.flix.core.models.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CadastrarVideoCommand {

	private final String titulo;
	private final String descricao;
	private final String url;
}
