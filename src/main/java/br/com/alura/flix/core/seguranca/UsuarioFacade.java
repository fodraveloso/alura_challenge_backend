package br.com.alura.flix.core.seguranca;

import java.util.Collection;

import org.springframework.stereotype.Service;

import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarUsuario;
import br.com.alura.flix.core.seguranca.ports.incoming.ObterUsuario;
import br.com.alura.flix.core.seguranca.ports.outgoing.UsuarioBancodados;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioFacade implements CadastrarUsuario, ObterUsuario {

	private final UsuarioBancodados usuarioBancodados;
	
	@Override
	public Collection<UsuarioDto> executar() {
		
		return usuarioBancodados.obterListaUsuario();
	}

	@Override
	public void executar(CadastrarUsuarioCommand command) {

		usuarioBancodados.cadastrarUsuario(command);
	}
}