package br.com.alura.flix.core.categorias.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriaDto {

	private Long id;
	private String titulo;
	private String cor;
	
}
