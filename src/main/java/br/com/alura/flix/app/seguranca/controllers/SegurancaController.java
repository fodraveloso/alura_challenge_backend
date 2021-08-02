package br.com.alura.flix.app.seguranca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.app.seguranca.models.CadastrarFuncaoRequest;
import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seguranca")
public class SegurancaController {

	private final CadastrarFuncao cadastrarFuncao;
	
	@PostMapping("/funcao")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarFuncao(@RequestBody @Validated CadastrarFuncaoRequest request) {
		
		cadastrarFuncao.executar(new CadastrarFuncaoCommand(request.getNome()));
	}
}
