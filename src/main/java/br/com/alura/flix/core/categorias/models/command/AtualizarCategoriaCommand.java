package br.com.alura.flix.core.categorias.models.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AtualizarCategoriaCommand {

	private final Long id;
	private final String titulo;
	private final String cor;
}