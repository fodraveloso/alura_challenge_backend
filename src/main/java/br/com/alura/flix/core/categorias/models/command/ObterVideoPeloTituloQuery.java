package br.com.alura.flix.core.categorias.models.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ObterVideoPeloTituloQuery {

	private final String titulo;
}
