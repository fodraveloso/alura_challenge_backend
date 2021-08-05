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

import br.com.alura.flix.core.seguranca.UsuarioFacade;
import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;
import br.com.alura.flix.core.seguranca.ports.outgoing.UsuarioBancodados;

@ExtendWith(MockitoExtension.class)
@DisplayName("Core: Usuario")
class UsuarioFacadeTest {

	@Mock
	private UsuarioBancodados usuarioBancodados;
	
	@InjectMocks
	private UsuarioFacade usuarioFacade;
	
	@Test
	@DisplayName("Tenta cadastrar usuario")
	void cadastrarUsuario() {
		
		UsuarioDto usuarioDto = new UsuarioDto(1L, "joao.veloso");
		
		CadastrarUsuarioCommand command = new CadastrarUsuarioCommand("joao.veloso", "123qwe!@#");
		
		doReturn(usuarioDto).when(usuarioBancodados).cadastrarUsuario(any(CadastrarUsuarioCommand.class));
		
		assertDoesNotThrow(() -> usuarioFacade.executar(command));
		
		verify(usuarioBancodados).cadastrarUsuario(any(CadastrarUsuarioCommand.class));
	}
	
	@Test
	@DisplayName("Tenta obter lista de usuário")
	void obterListaUsuario() {
		
		UsuarioDto usuarioDto = new UsuarioDto(1L, "joao.veloso");
		
		doReturn(List.of(usuarioDto)).when(usuarioBancodados).obterListaUsuario();
		
		Collection<UsuarioDto> usuariosObtidos = usuarioFacade.executar();
		
		verify(usuarioBancodados).obterListaUsuario();
		
		UsuarioDto usuarioObtido = usuariosObtidos.iterator().next();
		
		assertEquals(usuarioDto.getId(), usuarioObtido.getId());
		assertEquals(usuarioDto.getUsuario(), usuarioObtido.getUsuario());
	}
}
