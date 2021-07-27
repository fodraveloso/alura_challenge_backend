package br.com.alura.flix.app.categorias.validations.impl;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.alura.flix.app.categorias.models.AtualizarCategoriaRequest;
import br.com.alura.flix.app.categorias.validations.AtualizarCategoriaValidation;

public class AtualizarCategoriaValidationImpl
		implements ConstraintValidator<AtualizarCategoriaValidation, AtualizarCategoriaRequest> {

	@Override
	public boolean isValid(AtualizarCategoriaRequest value, ConstraintValidatorContext context) {

		return !(Objects.isNull(value.getCor()) && Objects.isNull(value.getTitulo()));
	}

}
