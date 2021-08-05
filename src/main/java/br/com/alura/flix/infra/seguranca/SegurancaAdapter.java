package br.com.alura.flix.infra.seguranca;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;
import br.com.alura.flix.core.seguranca.ports.outgoing.FuncaoBancodados;
import br.com.alura.flix.core.seguranca.ports.outgoing.UsuarioBancodados;
import br.com.alura.flix.infra.seguranca.entities.FuncaoEntity;
import br.com.alura.flix.infra.seguranca.entities.UsuarioEntity;
import br.com.alura.flix.infra.seguranca.repositories.FuncaoRepository;
import br.com.alura.flix.infra.seguranca.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SegurancaAdapter implements FuncaoBancodados, UsuarioBancodados, UserDetailsService {

	private final FuncaoRepository funcaoRepository;
	private final UsuarioRepository usuarioRepository;

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

	@Override
	public UsuarioDto cadastrarUsuario(CadastrarUsuarioCommand command) {

		UsuarioEntity save = usuarioRepository.save(new UsuarioEntity(command.getUsuario(), command.getSenha()));
		return new UsuarioDto(save.getId(), save.getUsername());
	}

	@Override
	public Collection<UsuarioDto> obterListaUsuario() {

		return usuarioRepository.findAll().stream()
				.map(usuario -> new UsuarioDto(usuario.getId(), usuario.getUsername())).collect(Collectors.toList());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return usuarioRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
}