package br.com.alura.flix.app;

import static br.com.alura.flix.utils.JsonCreator.startJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
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

import br.com.alura.flix.core.categorias.models.command.ObterVideoPeloTituloQuery;
import br.com.alura.flix.core.videos.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.videos.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.videos.models.query.ObterVideoQuery;
import br.com.alura.flix.core.videos.ports.incoming.VideosService;
import br.com.alura.flix.utils.JsonCreator;

@SpringBootTest
@DisplayName("App: Videos")
class VideosControllerTest {

	@MockBean
	private VideosService videosService;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private String tokenOwner;

	@BeforeEach
	void setup() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

		this.tokenOwner = this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
								.content(startJson().name("username").value("joao.veloso").name("password")
										.value("123qwe!@#").endJson()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}

	@Test
	@DisplayName("Tenta obter lista de videos com dados")
	void obterListaDeVideos_comDados() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo", 1L);

		doReturn(List.of(videoDto)).when(videosService).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/video").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isOk())
				.andExpect(jsonPath("[0].titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("[0].descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("[0].url").value(videoDto.getUrl()));

		verify(videosService, times(1)).executar();
	}

	@Test
	@DisplayName("Tenta obter lista de videos sem dados")
	void obterListaDeVideos_vazia() throws Exception {

		doReturn(List.of()).when(videosService).executar();

		mockMvc.perform(MockMvcRequestBuilders.get("/video").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isOk());

		verify(videosService, times(1)).executar();
	}

	@Test
	@DisplayName("Tenta obter video pelo ID")
	void obterVideoPeloId_Sucesso() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo", 1L);

		doReturn(videoDto).when(videosService).executar(any(ObterVideoQuery.class));

		mockMvc.perform(MockMvcRequestBuilders.get("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isOk())
				.andExpect(jsonPath("id").value(videoDto.getId()))
				.andExpect(jsonPath("titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("url").value(videoDto.getUrl()));

		verify(videosService, times(1)).executar(any(ObterVideoQuery.class));
	}

	@Test
	@DisplayName("Tenta obter video pelo ID que não existe")
	void obterVideoPeloId_erro() throws Exception {

		doThrow(new VideoNaoExisteException(1L)).when(videosService).executar(any(ObterVideoQuery.class));

		mockMvc.perform(MockMvcRequestBuilders.get("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isNotFound())
				.andExpect(jsonPath("codigo").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("erros[0]").value(String.format("Vídeo com id '%s' não existe", 1L)));
	}

	@Test
	@DisplayName("Tenta deletar vídeo pelo id")
	void deletarVideoPeloId() throws Exception {

		doNothing().when(videosService).executar(any(DeletarVideoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.delete("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta deletar video com id que não existe")
	void deletarVideoComIdQueNaoExiste() throws Exception {

		doThrow(new VideoNaoExisteException(1L)).when(videosService).executar(any(DeletarVideoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.delete("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Tenta cadastrar novo vídeo")
	void cadastrarVideo() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo", 1L);

		doReturn(videoDto).when(videosService).executar(any(CadastrarVideoCommand.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner).content(JsonCreator.startJson().name("titulo").value("Título")
						.name("descricao").value("Descrição").name("url").value("http://link.com/1").endJson()))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Tenta cadastrar novo vídeo com request inválido")
	void cadastrarVideo_erroPayload() throws Exception {

		mockMvc.perform(
				MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", tokenOwner).content(JsonCreator.startJson().name("titulo")
								.name("descricao").value("Descrição").name("url").value("Link").endJson()))
				.andExpect(status().isBadRequest());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", tokenOwner).content(JsonCreator.startJson().name("titulo")
								.name("descricao").value("Descrição").name("url").value("Link").endJson()))
				.andExpect(status().isBadRequest());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", tokenOwner).content(JsonCreator.startJson().name("titulo")
								.value("Título").name("descricao").value("").name("url").endJson()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tenta atualizar título do vídeo")
	void atualizarTituloVideo() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)
				.content(JsonCreator.startJson().name("titulo").value("Novo Titulo").endJson()))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta atualizar descricao do vídeo")
	void atualizarDescricaoVideo() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)
				.content(JsonCreator.startJson().name("descricao").value("Nova Descrição").endJson()))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta atualizar link do vídeo")
	void atualizarLinkVideo() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)
				.content(JsonCreator.startJson().name("url").value("http://link.com/1").endJson()))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Tenta atualizar video com payload invalido")
	void atualizarVideoComPayloadInvalido() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.put("/video/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner).content(JsonCreator.startJson().endJson()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tenta obter videos com query")
	void obterVideoPorQuery() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Título", "Descrição", "Http://url.com.br/videos/1", 1L);

		doReturn(List.of(videoDto)).when(videosService).executar(any(ObterVideoPeloTituloQuery.class));

		mockMvc.perform(MockMvcRequestBuilders.get("/video?search=jogos").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", tokenOwner)).andExpect(status().isOk())
				.andExpect(jsonPath("[0].id").value(videoDto.getId()))
				.andExpect(jsonPath("[0].titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("[0].descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("[0].url").value(videoDto.getUrl()))
				.andExpect(jsonPath("[0].categoriaId").value(videoDto.getCategoriaId()));

	}
}
