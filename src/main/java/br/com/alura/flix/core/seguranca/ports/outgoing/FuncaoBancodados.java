package br.com.alura.flix.core.seguranca.ports.outgoing;

import java.util.Collection;

import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;

public interface FuncaoBancodados {

	FuncaoDto cadastrarFuncao(CadastrarFuncaoCommand command);

	Collection<FuncaoDto> obterListaDeFuncoes();

}
