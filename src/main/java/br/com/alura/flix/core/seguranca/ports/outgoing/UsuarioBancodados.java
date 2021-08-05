package br.com.alura.flix.core.seguranca.ports.outgoing;

import java.util.Collection;

import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;

public interface UsuarioBancodados {

	UsuarioDto cadastrarUsuario(CadastrarUsuarioCommand command);

	Collection<UsuarioDto> obterListaUsuario();

}
