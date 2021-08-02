package br.com.alura.flix.core.categorias.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.seguranca.models.FuncaoDto;

public interface ObterFuncao {

	Collection<FuncaoDto> executar();

}
