package br.com.alura.flix.core.seguranca.ports.incoming;

import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;

public interface CadastrarUsuario {

	void executar(CadastrarUsuarioCommand command);

}
