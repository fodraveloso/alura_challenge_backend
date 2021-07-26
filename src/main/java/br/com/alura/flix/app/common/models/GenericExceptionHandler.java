package br.com.alura.flix.app.common.models;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GenericExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErroResponse validacaoPayload(MethodArgumentNotValidException exception) {

		var erroResponse = new ErroResponse(HttpStatus.BAD_REQUEST.value());
		erroResponse.getErros().addAll(exception.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.toCollection(ArrayList::new)));
		return erroResponse;
	}
}
