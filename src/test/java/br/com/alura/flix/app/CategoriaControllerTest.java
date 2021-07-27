package br.com.alura.flix.app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.alura.flix.app.categorias.controllers.CategoriaController;
import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.models.query.ObterVideosPorCategoriaQuery;
import br.com.alura.flix.core.categorias.ports.incoming.CategoriaService;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.utils.JsonCreator;

@WebMvcTest(CategoriaController.class)
@DisplayName("App: Categoria")
class CategoriaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoriaService categoriaService;

	@Test
	@DisplayName("Tenta cadastrar categoria")
	void cadastrarCategoria() throws Exception {

		doReturn(new CategoriaDto(1L, "Título da categoria", "Cor da categoria")).when(categoriaService)
				.executar(any(CadastrarCategoriaCommand.class));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON)
								.content(JsonCreator.startJson().name("titulo").value("Titutlo da categoria")
										.name("cor").value("Cor da categoria").endJson()))
				.andExpect(status().isCreated());

		verify(categoriaService, times(1)).executar(any(CadastrarCategoriaCommand.class));
	}

	@Test
	@DisplayName("Tenta pesquisar caregoria pelo id")
	void obterCategoriaPeloId() throws Exception {

		CategoriaDto categoriaDto = new CategoriaDto(1L, "Título", "Cor");

		doReturn(categoriaDto).when(categoriaService).executar(any(ObterCategoriaPeloIdQuery.class));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("id").value(categoriaDto.getId()))
				.andExpect(jsonPath("titulo").value(categoriaDto.getTitulo()))
				.andExpect(jsonPath("cor").value(categoriaDto.getCor()));

		verify(categoriaService, times(1)).executar(any(ObterCategoriaPeloIdQuery.class));
	}

	@Test
	@DisplayName("Tenta obter lista de categorias")
	void obterListaDeCategorias() throws Exception {

		CategoriaDto categoriaDto = new CategoriaDto(1L, "Título", "Cor");

		doReturn(List.of(categoriaDto)).when(categoriaService).executar();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("[0].id").value(categoriaDto.getId()))
				.andExpect(jsonPath("[0].titulo").value(categoriaDto.getTitulo()))
				.andExpect(jsonPath("[0].cor").value(categoriaDto.getCor()));

		verify(categoriaService, times(1)).executar();
	}

	@Test
	@DisplayName("Tenta atualizar categoria pelo id")
	void atualizarCategoriaPeloId() throws Exception {

		CategoriaDto categoriaDto = new CategoriaDto(1L, "Novo Título", "Nova Cor");

		doReturn(categoriaDto).when(categoriaService).executar(any(AtualizarCategoriaCommand.class));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/categorias/1").contentType(MediaType.APPLICATION_JSON)
						.content(JsonCreator.startJson().name("id").value("1").name("titulo")
								.value("Titutlo da categoria").name("cor").value("Cor da categoria").endJson()))
				.andExpect(status().isOk()).andExpect(jsonPath("id").value(categoriaDto.getId()))
				.andExpect(jsonPath("titulo").value(categoriaDto.getTitulo()))
				.andExpect(jsonPath("cor").value(categoriaDto.getCor()));

		verify(categoriaService, times(1)).executar(any(AtualizarCategoriaCommand.class));
	}

	@Test
	@DisplayName("Tenta atualizar categoria pelo id com dados invalidos")
	void atualizarCategoriaPeloIdComDadosInvalidos() throws Exception {

		CategoriaDto categoriaDto = new CategoriaDto(1L, "Novo Título", "Nova Cor");

		doReturn(categoriaDto).when(categoriaService).executar(any(AtualizarCategoriaCommand.class));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/categorias/1").contentType(MediaType.APPLICATION_JSON)
						.content(JsonCreator.startJson().name("titulo").name("cor").endJson()))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tenta apagar categoria pelo id")
	void apagarCategoriaPeloId() throws Exception {

		doNothing().when(categoriaService).executar(any(ApagarCategoriaCommand.class));

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/categorias/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(categoriaService, times(1)).executar(any(ApagarCategoriaCommand.class));
	}

	@Test
	@DisplayName("Tenta cadastrar categoria com request incompleto")
	void cadastrarCategoriaComRequestInvalido() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").name("cor").value("Cor da categoria").endJson()))
				.andExpect(status().isBadRequest());

		verify(categoriaService, times(0)).executar(any(CadastrarCategoriaCommand.class));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/categorias").contentType(MediaType.APPLICATION_JSON).content(
						JsonCreator.startJson().name("titulo").value("Titulo da Categoria").name("cor").endJson()))
				.andExpect(status().isBadRequest());

		verify(categoriaService, times(0)).executar(any(CadastrarCategoriaCommand.class));
	}

	@Test
	@DisplayName("Tenta obter lista de videos por categoria")
	void obterListaDeVideosPorCategoria() throws Exception {

		VideoDto videoDto = new VideoDto(1L, "Titulo do Vídeo", "Descrição do vídeo", "URL do vídeo", 1L);

		doReturn(List.of(videoDto)).when(categoriaService).executar(any(ObterVideosPorCategoriaQuery.class));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/categorias/1/videos").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("[0].id").value(videoDto.getId()))
				.andExpect(jsonPath("[0].titulo").value(videoDto.getTitulo()))
				.andExpect(jsonPath("[0].descricao").value(videoDto.getDescricao()))
				.andExpect(jsonPath("[0].url").value(videoDto.getUrl()))
				.andExpect(jsonPath("[0].categoriaId").value(videoDto.getCategoriaId()));
	}
}
