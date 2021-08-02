package br.com.alura.flix.core.seguranca.ports.outgoing;

import java.util.Collection;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;

public interface FuncaoBancodados {

	FuncaoDto cadastrarFuncao(CadastrarFuncaoCommand command);

	Collection<FuncaoDto> obterListaDeFuncoes();

}
