package br.com.alura.flix.core.seguranca.ports.incoming;

import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;

public interface CadastrarFuncao {

	void executar(CadastrarFuncaoCommand command);

}
