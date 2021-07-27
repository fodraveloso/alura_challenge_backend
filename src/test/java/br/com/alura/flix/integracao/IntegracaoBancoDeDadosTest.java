package br.com.alura.flix.integracao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.flix.app.categorias.controllers.CategoriaController;
import br.com.alura.flix.app.categorias.models.CadastrarCategoriaRequest;
import br.com.alura.flix.app.videos.controllers.VideosController;
import br.com.alura.flix.app.videos.models.CadastrarVideoRequest;
import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.utils.CustomPostgresContainer;
import br.com.alura.flix.utils.JsonCreator;

@SpringBootTest
@Testcontainers
@ContextConfiguration
@SuppressWarnings("rawtypes")
class IntegracaoBancoDeDadosTest {

	@Container
	private static final PostgreSQLContainer CONTAINER = CustomPostgresContainer.getInstance();

	@Autowired
	private CategoriaController categoriaController;

	@Autowired
	private VideosController videosController;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@DisplayName("Tenta validar se container de banco de dados esta funcionando")
	void testaContainer() {

		assertTrue(CONTAINER.isRunning());
	}

	@Test
	@DisplayName("Tenta testar fluxo de criacao de videos")
	void fluxoDeCriacaoDeVideos() throws Exception {

		CadastrarCategoriaRequest requestCategoriaLivre = mapper.readValue(
				JsonCreator.startJson().name("titulo").value("LIVRE").name("cor").value("Azul").endJson(),
				CadastrarCategoriaRequest.class);

		CategoriaDto categoriaLivreDto = categoriaController.cadastrarCategoria(requestCategoriaLivre);

		CadastrarVideoRequest requestVideoTestcontainer = mapper
				.readValue(JsonCreator.startJson().name("titulo").value("Executando testes de integração")
						.name("descricao").value("Passo a passo para a utilização de testcontainer").name("url")
						.value("http://url.com.br/video/1").name("categoriaId")
						.value(categoriaLivreDto.getId().toString()).endJson(), CadastrarVideoRequest.class);
		
		videosController.cadastrarVideo(requestVideoTestcontainer);
		
		Collection<VideoDto> videoObtidosPelaCategoria = categoriaController.obterListaDeVideosPorCategoria(categoriaLivreDto.getId());
		
		assertFalse(videoObtidosPelaCategoria.isEmpty());
	}

}
