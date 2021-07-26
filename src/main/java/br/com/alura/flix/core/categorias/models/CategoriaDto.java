package br.com.alura.flix.core.categorias.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoriaDto {

	private final Long id;
	private final String titulo;
	private final String cor;
}
