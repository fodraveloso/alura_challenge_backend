package br.com.alura.flix.app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.alura.flix.app.seguranca.controllers.SegurancaController;
import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.categorias.ports.incoming.ObterFuncao;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import br.com.alura.flix.utils.JsonCreator;

@WebMvcTest(SegurancaController.class)
@DisplayName("App: Segurança")
class FuncaoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CadastrarFuncao cadastrarFuncao;

	@MockBean
	private ObterFuncao obterFuncao;

	@Test
	@DisplayName("Tenta cadastrar função")
	void cadastrarFuncao() throws Exception {

		doNothing().when(cadastrarFuncao).executar(any(CadastrarFuncaoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/seguranca/funcao").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("nome").value("ROLE_OWNER").endJson()))
				.andExpect(status().isCreated());

		verify(cadastrarFuncao).executar(any(CadastrarFuncaoCommand.class));
	}

	@Test
	@DisplayName("Tenta listar funções cadastradas")
	void listarFuncoes() throws Exception {

		FuncaoDto funcaoDto = new FuncaoDto(1L, "ROLE_OWNER");

		doReturn(List.of(funcaoDto)).when(obterFuncao).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/seguranca/funcao")).andExpect(status().isOk())
				.andExpect(jsonPath("[0].id").value(funcaoDto.getId()))
				.andExpect(jsonPath("[0].nome").value(funcaoDto.getNome()));
		
		verify(obterFuncao).executar();
	}
}
