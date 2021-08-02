package br.com.alura.flix.app.seguranca.models;

import javax.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class CadastrarFuncaoRequest {

	@Pattern(regexp = "ROLE_.*")
	private String nome;
}
