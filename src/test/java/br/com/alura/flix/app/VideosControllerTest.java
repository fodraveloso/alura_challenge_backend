package br.com.alura.flix.app;

import static org.mockito.Mockito.doReturn;
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

import br.com.alura.flix.app.controllers.VideosController;
import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.ports.incoming.ObterVideos;

@WebMvcTest(VideosController.class)
class VideosControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ObterVideos obterVideos;

	@Test
	@DisplayName("Tenta obter lista de videos com dados")
	void obterListaDeVideos_comDados() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo");

		doReturn(List.of(videoDto)).when(obterVideos).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/video").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("[0].titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("[0].descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("[0].url").value(videoDto.getUrl()));
	}

	@Test
	@DisplayName("Tenta obter lista de videos sem dados")
	void obterListaDeVideos_vazia() throws Exception {

		doReturn(List.of()).when(obterVideos).executar();
		
		mockMvc.perform(MockMvcRequestBuilders.get("/video").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
