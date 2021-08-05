package br.com.alura.flix.core.seguranca.models.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CadastrarUsuarioCommand {

	private String usuario;
	private String senha;
}
