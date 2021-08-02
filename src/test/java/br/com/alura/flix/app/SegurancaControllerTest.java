package br.com.alura.flix.app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.alura.flix.core.seguranca.ports.incoming.CadastrarFuncao;
import br.com.alura.flix.utils.JsonCreator;

@WebMvcTest(SegurancaController.class)
@DisplayName("App: Segurança")
class SegurancaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CadastrarFuncao cadastrarFuncao;

	@Test
	@DisplayName("Tenta cadastrar função")
	void cadastrarFuncao() throws Exception {

		doNothing().when(cadastrarFuncao).executar(any(CadastrarFuncaoCommand.class));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/seguranca/funcao").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("nome").value("ROLE_OWNER").endJson()))
				.andExpect(status().isCreated());
		
		verify(cadastrarFuncao).executar(any(CadastrarFuncaoCommand.class));
	}
}
