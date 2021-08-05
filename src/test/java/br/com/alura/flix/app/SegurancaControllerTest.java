package br.com.alura.flix.app;

import static br.com.alura.flix.utils.JsonCreator.startJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.alura.flix.core.categorias.ports.incoming.ObterFuncao;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarUsuario;
import br.com.alura.flix.core.seguranca.ports.incoming.ObterUsuario;
import br.com.alura.flix.utils.JsonCreator;

@SpringBootTest
@DisplayName("App: Segurança")
class SegurancaControllerTest {

	private MockMvc mockMvc;
	private String tokenAdmin;

	@Autowired
	private WebApplicationContext wac;

	@MockBean
	private CadastrarFuncao cadastrarFuncao;

	@MockBean
	private ObterFuncao obterFuncao;

	@MockBean
	private CadastrarUsuario cadastrarUsuario;

	@MockBean
	private ObterUsuario obterUsuario;

	@BeforeEach
	void setup() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

		this.tokenAdmin = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(
						startJson().name("username").value("joao.fodra").name("password").value("!@#qwe123").endJson()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}

	@Test
	@DisplayName("Tenta cadastrar função")
	void cadastrarFuncao() throws Exception {

		doNothing().when(cadastrarFuncao).executar(any(CadastrarFuncaoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/seguranca/funcao").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenAdmin).content(startJson().name("nome").value("ROLE_OWNER").endJson()))
				.andExpect(status().isCreated());

		verify(cadastrarFuncao).executar(any(CadastrarFuncaoCommand.class));
	}

	@Test
	@DisplayName("Tenta listar funções cadastradas")
	void listarFuncoes() throws Exception {

		FuncaoDto funcaoDto = new FuncaoDto(1L, "ROLE_OWNER");

		doReturn(List.of(funcaoDto)).when(obterFuncao).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/seguranca/funcao").header("Authorization", tokenAdmin))
				.andExpect(status().isOk()).andExpect(jsonPath("[0].id").value(funcaoDto.getId()))
				.andExpect(jsonPath("[0].nome").value(funcaoDto.getNome()));

		verify(obterFuncao).executar();
	}

	@Test
	@DisplayName("Tenta cadastrar usuario")
	void cadastrarUsuario() throws Exception {

		doNothing().when(cadastrarUsuario).executar(any(CadastrarUsuarioCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/seguranca/usuario").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenAdmin)
				.content(startJson().name("usuario").value("joao.veloso").name("senha").value("123qwe!@#").endJson()))
				.andExpect(status().isCreated());

		verify(cadastrarUsuario).executar(any(CadastrarUsuarioCommand.class));
	}

	@Test
	@DisplayName("Tenta obter lista de usuários")
	void obterListaDeUsuarios() throws Exception {

		UsuarioDto usuarioDto = new UsuarioDto(1L, "joao.veloso");

		doReturn(List.of(usuarioDto)).when(obterUsuario).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/seguranca/usuario").header("Authorization", tokenAdmin))
				.andExpect(status().isOk()).andExpect(jsonPath("[0].id").value(1L))
				.andExpect(jsonPath("[0].usuario").value("joao.veloso"));

		verify(obterUsuario).executar();
	}

	@Test
	@DisplayName("Tenta obter token com credenciais invalidas")
	void tentaObterTokenComCredenciaisInvalidas() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(
						startJson().name("username").value("joao.invalido").name("password").value("000000").endJson()))
				.andExpect(status().isUnauthorized());
	}
}
