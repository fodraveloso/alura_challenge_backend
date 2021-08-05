package br.com.alura.flix.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.flix.core.seguranca.FuncaoFacade;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.ports.outgoing.FuncaoBancodados;

@ExtendWith(MockitoExtension.class)
@DisplayName("Core: Segurança")
class FuncaoFacadeTest {

	@Mock
	private FuncaoBancodados funcaoBancodados;

	@InjectMocks
	private FuncaoFacade segurancaFacade;

	@Test
	@DisplayName("Tenta cadastrar função")
	void cadastrarFuncao() {

		FuncaoDto funcaoDto = new FuncaoDto(1L, "ROLE_OWNER");

		CadastrarFuncaoCommand command = new CadastrarFuncaoCommand("ROLE_OWNER");

		doReturn(funcaoDto).when(funcaoBancodados).cadastrarFuncao(any(CadastrarFuncaoCommand.class));

		assertDoesNotThrow(() -> segurancaFacade.executar(command));

		verify(funcaoBancodados).cadastrarFuncao(any(CadastrarFuncaoCommand.class));
	}

	@Test
	@DisplayName("Tenta obter lista de funçoes")
	void obterFuncao() {
		
		FuncaoDto funcaoDto = new FuncaoDto(1L, "ROLE_OWNER");
		
		doReturn(List.of(funcaoDto)).when(funcaoBancodados).obterListaDeFuncoes();
		
		Collection<FuncaoDto> funcoesObtidas = segurancaFacade.executar();
		
		verify(funcaoBancodados).obterListaDeFuncoes();
		
		assertEquals(1, funcoesObtidas.size());
		
		FuncaoDto funcaoObtida = funcoesObtidas.iterator().next();
		
		assertEquals(funcaoDto.getId(), funcaoObtida.getId());
		assertEquals(funcaoDto.getNome(), funcaoObtida.getNome());
	}
}
