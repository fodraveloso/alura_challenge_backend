package br.com.alura.flix.core.seguranca.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioDto {

	private Long id;
	private String usuario;
}
