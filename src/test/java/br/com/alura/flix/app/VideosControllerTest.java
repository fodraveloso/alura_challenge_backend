package br.com.alura.flix.app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.alura.flix.app.controllers.VideosController;
import br.com.alura.flix.core.exceptions.DadosParaAtualizacaoIncorretos;
import br.com.alura.flix.core.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.models.query.ObterVideoQuery;
import br.com.alura.flix.core.ports.incoming.VideosService;
import br.com.alura.flix.utils.JsonCreator;

@WebMvcTest(VideosController.class)
@DisplayName("App: Videos")
class VideosControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VideosService videosService;

	@Test
	@DisplayName("Tenta obter lista de videos com dados")
	void obterListaDeVideos_comDados() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo");

		doReturn(List.of(videoDto)).when(videosService).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/video").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("[0].titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("[0].descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("[0].url").value(videoDto.getUrl()));

		verify(videosService, times(1)).executar();
	}

	@Test
	@DisplayName("Tenta obter lista de videos sem dados")
	void obterListaDeVideos_vazia() throws Exception {

		doReturn(List.of()).when(videosService).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/video").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(videosService, times(1)).executar();
	}

	@Test
	@DisplayName("Tenta obter video pelo ID")
	void obterVideoPeloId_Sucesso() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo");

		doReturn(videoDto).when(videosService).executar(any(ObterVideoQuery.class));

		mockMvc.perform(MockMvcRequestBuilders.get("/video/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("id").value(videoDto.getId()))
				.andExpect(jsonPath("titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("url").value(videoDto.getUrl()));

		verify(videosService, times(1)).executar(any(ObterVideoQuery.class));
	}

	@Test
	@DisplayName("Tenta obter video pelo ID que não existe")
	void obterVideoPeloId_erro() throws Exception {

		doThrow(new VideoNaoExisteException(1L)).when(videosService).executar(any(ObterVideoQuery.class));

		mockMvc.perform(MockMvcRequestBuilders.get("/video/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("codigo").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("erros[0]").value(String.format("Vídeo com id '%s' não existe", 1L)));
	}

	@Test
	@DisplayName("Tenta deletar vídeo pelo id")
	void deletarVideoPeloId() throws Exception {

		doNothing().when(videosService).executar(any(DeletarVideoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.delete("/video/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta deletar video com id que não existe")
	void deletarVideoComIdQueNaoExiste() throws Exception {

		doThrow(new VideoNaoExisteException(1L)).when(videosService).executar(any(DeletarVideoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.delete("/video/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Tenta cadastrar novo vídeo")
	void cadastrarVideo() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo");

		doReturn(videoDto).when(videosService).executar(any(CadastrarVideoCommand.class));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
						.content(JsonCreator.startJson().name("titulo").value("Título").name("descricao")
								.value("Descrição").name("url").value("Link do vídeo").endJson()))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Tenta cadastrar novo vídeo com request inválido")
	void cadastrarVideo_erroPayload() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("titulo").name("descricao").value("Descrição").name("url")
						.value("Link").endJson()))
				.andExpect(status().isBadRequest());

		mockMvc.perform(MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("titulo").name("descricao").value("Descrição").name("url")
						.value("Link").endJson()))
				.andExpect(status().isBadRequest());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON).content(JsonCreator
						.startJson().name("titulo").value("Título").name("descricao").value("").name("url").endJson()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tenta atualizar título do vídeo")
	void atualizarTituloVideo() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("titulo").value("Novo Titulo").endJson()))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta atualizar descricao do vídeo")
	void atualizarDescricaoVideo() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("descricao").value("Nova Descrição").endJson()))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta atualizar link do vídeo")
	void atualizarLinkVideo() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("url").value("Novo Link").endJson())).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta atualizar video com payload invalido")
	void atualizarVideoComPayloadInvalido() throws Exception {

		doThrow(new DadosParaAtualizacaoIncorretos()).when(videosService).executar(any(AtualizarVideoCommand.class));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().endJson())).andExpect(status().isBadRequest());
	}
}
