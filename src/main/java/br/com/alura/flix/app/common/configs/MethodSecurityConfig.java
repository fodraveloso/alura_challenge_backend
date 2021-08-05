package br.com.alura.flix.app.common.configs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	private final ApplicationContext applicationContext;
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
				
		var handler = new OAuth2MethodSecurityExpressionHandler();
		handler.setApplicationContext(applicationContext);
		return handler;
	}
}
