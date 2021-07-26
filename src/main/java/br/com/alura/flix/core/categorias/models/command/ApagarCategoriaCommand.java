package br.com.alura.flix.core.categorias.models.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApagarCategoriaCommand {

	private final Long id;
}
