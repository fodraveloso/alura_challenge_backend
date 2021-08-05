package br.com.alura.flix.core.seguranca.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.seguranca.models.UsuarioDto;

public interface ObterUsuario {

	Collection<UsuarioDto> executar();

}
