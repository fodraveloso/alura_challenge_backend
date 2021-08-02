package br.com.alura.flix.app.seguranca;

import org.springframework.stereotype.Service;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import br.com.alura.flix.core.seguranca.ports.outgoing.FuncaoBancodados;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SegurancaFacade implements CadastrarFuncao {

	private final FuncaoBancodados funcaoBancodados;

	@Override
	public void executar(CadastrarFuncaoCommand command) {
		
		funcaoBancodados.cadastrarFuncao(command);
	}
}
