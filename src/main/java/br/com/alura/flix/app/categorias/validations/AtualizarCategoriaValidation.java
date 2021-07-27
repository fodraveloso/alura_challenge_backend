package br.com.alura.flix.app.categorias.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.alura.flix.app.categorias.validations.impl.AtualizarCategoriaValidationImpl;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtualizarCategoriaValidationImpl.class)
public @interface AtualizarCategoriaValidation {


	  String message() default "";
	  Class <?> [] groups() default {};
	  Class <? extends Payload> [] payload() default {};
}
