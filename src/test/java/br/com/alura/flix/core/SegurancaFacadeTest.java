package br.com.alura.flix.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.flix.app.seguranca.SegurancaFacade;
import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.ports.outgoing.FuncaoBancodados;

@ExtendWith(MockitoExtension.class)
@DisplayName("Core: Segurança")
class SegurancaFacadeTest {

	@Mock
	private FuncaoBancodados funcaoBancodados;
	
	@InjectMocks
	private SegurancaFacade segurancaFacade;
	
	@Test
	@DisplayName("Tenta cadastrar função")
	void cadastrarFuncao() {
		
		FuncaoDto funcaoDto = new FuncaoDto(1L, "ROLE_OWNER");
		
		CadastrarFuncaoCommand command = new CadastrarFuncaoCommand("ROLE_OWNER");
		
		doReturn(funcaoDto).when(funcaoBancodados).cadastrarFuncao(any(CadastrarFuncaoCommand.class));
		
		assertDoesNotThrow(() -> segurancaFacade.executar(command));
		
		verify(funcaoBancodados).cadastrarFuncao(any(CadastrarFuncaoCommand.class));
	}
}
