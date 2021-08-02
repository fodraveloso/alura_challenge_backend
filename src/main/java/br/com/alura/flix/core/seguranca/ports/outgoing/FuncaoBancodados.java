package br.com.alura.flix.core.seguranca.ports.outgoing;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;

public interface FuncaoBancodados {

	FuncaoDto cadastrarFuncao(CadastrarFuncaoCommand command);

}
