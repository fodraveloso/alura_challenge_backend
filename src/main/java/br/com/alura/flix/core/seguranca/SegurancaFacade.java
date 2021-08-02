package br.com.alura.flix.core.seguranca;

import java.util.Collection;

import org.springframework.stereotype.Service;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.categorias.ports.incoming.ObterFuncao;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import br.com.alura.flix.core.seguranca.ports.outgoing.FuncaoBancodados;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SegurancaFacade implements CadastrarFuncao, ObterFuncao {

	private final FuncaoBancodados funcaoBancodados;

	@Override
	public void executar(CadastrarFuncaoCommand command) {
		
		funcaoBancodados.cadastrarFuncao(command);
	}

	@Override
	public Collection<FuncaoDto> executar() {

		return funcaoBancodados.obterListaDeFuncoes();
	}
}
