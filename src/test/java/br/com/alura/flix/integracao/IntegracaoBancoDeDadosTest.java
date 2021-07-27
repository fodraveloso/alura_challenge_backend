package br.com.alura.flix.integracao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.utils.CustomPostgresContainer;
import br.com.alura.flix.utils.JsonCreator;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration
@SuppressWarnings("rawtypes")
@TestMethodOrder(OrderAnnotation.class)
class IntegracaoBancoDeDadosTest {

	@Container
	private static final PostgreSQLContainer CONTAINER = CustomPostgresContainer.getInstance();

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setup() throws Exception {

		String responseVideos = this.mockMvc.perform(MockMvcRequestBuilders.get("/video")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		Arrays.asList(mapper.readValue(responseVideos, VideoDto[].class)).forEach(v -> {
			try {
				this.mockMvc.perform(MockMvcRequestBuilders.delete("/video/" + v.getId())).andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		String responseCategorias = this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Arrays.asList(mapper.readValue(responseCategorias, CategoriaDto[].class)).forEach(v -> {
			try {
				this.mockMvc.perform(MockMvcRequestBuilders.delete("/categorias/" + v.getId()))
						.andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	@DisplayName("Tenta criar categorias")
	void criarCategorias() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").value("LIVRE").name("cor").value("Azul").endJson()))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").value("API").name("cor").value("Laranja").endJson()))
				.andExpect(status().isCreated());

		String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<CategoriaDto> asList = Arrays.asList(mapper.readValue(response, CategoriaDto[].class));

		assertEquals(2, asList.size());
	}

	@Test
	@DisplayName("Tenata criar video com categoria")
	void criarVideoComCategoria() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").value("API").name("cor").value("Laranja").endJson()))
				.andExpect(status().isCreated());

		String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<CategoriaDto> asList = Arrays.asList(mapper.readValue(response, CategoriaDto[].class));

		assertEquals(1, asList.size());

		CategoriaDto categoriaDto = asList.iterator().next();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("titulo").value("Como cadastrar categorias").name("descricao")
						.value("Passo a passo para o cadastro de categorias").name("url").value("http://link.com/1")
						.name("categoriaId").value(categoriaDto.getId().toString()).endJson()))
				.andExpect(status().isCreated());

		String responseVideos = this.mockMvc.perform(MockMvcRequestBuilders.get("/video")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<VideoDto> videos = Arrays.asList(mapper.readValue(responseVideos, VideoDto[].class));
		assertEquals(1, videos.size());

		String responseVideosPorCategoria = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/categorias/" + categoriaDto.getId() + "/videos"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		List<VideoDto> videosPorCategoria = Arrays
				.asList(mapper.readValue(responseVideosPorCategoria, VideoDto[].class));
		assertEquals(1, videosPorCategoria.size());
	}

	@Test
	@DisplayName("Tenta criar video sem categoria")
	void criarVideoSeCategoria() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").value("LIVRE").name("cor").value("Azul").endJson()))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").value("API").name("cor").value("Laranja").endJson()))
				.andExpect(status().isCreated());

		this.mockMvc.perform(MockMvcRequestBuilders.post("/video").contentType(MediaType.APPLICATION_JSON)
				.content(JsonCreator.startJson().name("titulo").value("Como cadastrar categorias").name("descricao")
						.value("Passo a passo para o cadastro de categorias").name("url").value("http://link.com/1")
						.endJson()))
				.andExpect(status().isCreated());

		String responseVideos = this.mockMvc.perform(MockMvcRequestBuilders.get("/video")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<VideoDto> videos = Arrays.asList(mapper.readValue(responseVideos, VideoDto[].class));
		assertEquals(1, videos.size());

		VideoDto video = videos.iterator().next();

		String responseCategorias = this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias/" + video.getCategoriaId()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		CategoriaDto categoria = mapper.readValue(responseCategorias, CategoriaDto.class);
		assertEquals("LIVRE", categoria.getTitulo());
	}

}
