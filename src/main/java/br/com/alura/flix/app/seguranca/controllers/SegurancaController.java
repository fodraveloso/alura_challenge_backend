package br.com.alura.flix.app.seguranca.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.app.seguranca.models.CadastrarFuncaoRequest;
import br.com.alura.flix.app.seguranca.models.CadastrarUsuarioRequest;
import br.com.alura.flix.core.categorias.ports.incoming.ObterFuncao;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarUsuario;
import br.com.alura.flix.core.seguranca.ports.incoming.ObterUsuario;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seguranca")
public class SegurancaController {

	private final CadastrarFuncao cadastrarFuncao;
	private final ObterFuncao obterFuncao;
	private final CadastrarUsuario cadastrarUsuario;
	private final ObterUsuario obterUsuario;

	@PostMapping("/funcao")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured({"ROLE_ADMIN"})
	public void cadastrarFuncao(@RequestBody @Validated CadastrarFuncaoRequest request) {

		cadastrarFuncao.executar(new CadastrarFuncaoCommand(request.getNome()));
	}

	@GetMapping("/funcao")
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN"})
	public Collection<FuncaoDto> obterFuncao() {

		return obterFuncao.executar();
	}

	@PostMapping("/usuario")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured({"ROLE_ADMIN"})
	public void cadastrarUsuario(@RequestBody @Validated CadastrarUsuarioRequest request) {

		cadastrarUsuario.executar(new CadastrarUsuarioCommand(request.getUsuario(), request.getSenha()));
	}
	
	@GetMapping("/usuario")
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN"})
	public Collection<UsuarioDto> obterListaUsuario() {
		
		return obterUsuario.executar();
	}
}
