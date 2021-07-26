package br.com.alura.flix.core.categorias.models.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CadastrarCategoriaCommand {

	private final String titulo;
	private final String cor;
}
