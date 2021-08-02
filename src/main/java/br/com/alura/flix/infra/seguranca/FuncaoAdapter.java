package br.com.alura.flix.infra.seguranca;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.ports.outgoing.FuncaoBancodados;
import br.com.alura.flix.infra.seguranca.entities.FuncaoEntity;
import br.com.alura.flix.infra.seguranca.repositories.FuncaoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FuncaoAdapter implements FuncaoBancodados {

	private final FuncaoRepository funcaoRepository;

	@Override
	public FuncaoDto cadastrarFuncao(CadastrarFuncaoCommand command) {

		FuncaoEntity save = funcaoRepository.save(new FuncaoEntity(command.getNome()));
		return new FuncaoDto(save.getId(), save.getNome());
	}

	@Override
	public Collection<FuncaoDto> obterListaDeFuncoes() {

		return funcaoRepository.findAll().stream().map(funcao -> new FuncaoDto(funcao.getId(), funcao.getNome()))
				.collect(Collectors.toList());
	}
}