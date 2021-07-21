package br.com.alura.flix.app.validations.impl;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.alura.flix.app.models.AtualizarVideoRequest;
import br.com.alura.flix.app.validations.AtualizarVideoValidation;

public class AtualizarVideoValidationImpl implements ConstraintValidator<AtualizarVideoValidation, AtualizarVideoRequest> {

	@Override
	public boolean isValid(AtualizarVideoRequest value, ConstraintValidatorContext context) {

		return !(Objects.isNull(value.getTitulo()) && Objects.isNull(value.getDescricao()) && Objects.isNull(value.getUrl()));
	}

}
