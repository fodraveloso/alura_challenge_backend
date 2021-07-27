package br.com.alura.flix.core.categorias.models.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ObterVideosPorCategoriaQuery {

	private final Long categoriaId;
}
