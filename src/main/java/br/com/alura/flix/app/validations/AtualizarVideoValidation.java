package br.com.alura.flix.app.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.alura.flix.app.validations.impl.AtualizarVideoValidationImpl;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtualizarVideoValidationImpl.class)
public @interface AtualizarVideoValidation {

	  String message() default "{com.tericcabrel.hotel.constraints.BirthDate.message}";
	  Class <?> [] groups() default {};
	  Class <? extends Payload> [] payload() default {};
}
