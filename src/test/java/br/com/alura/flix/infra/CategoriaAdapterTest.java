package br.com.alura.flix.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.models.query.ObterVideosPorCategoriaQuery;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.infra.categorias.CategoriaAdapter;
import br.com.alura.flix.infra.categorias.entities.CategoriaEntity;
import br.com.alura.flix.infra.categorias.repositories.CategoriaRepository;
import br.com.alura.flix.infra.videos.entities.VideoEntity;

@DataJpaTest
@DisplayName("Infra: Categoria")
class CategoriaAdapterTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	private CategoriaAdapter categoriaAdapter;
	
	@BeforeEach
	void setup() {
		
		this.categoriaAdapter = new CategoriaAdapter(categoriaRepository);
	}
	
	@Test
	@DisplayName("Tenta cadastrar categoria")
	void cadastrarCategoria() {
		
		CadastrarCategoriaCommand command = new CadastrarCategoriaCommand("Titulo", "Cor");
		
		CategoriaDto categoriaDto = categoriaAdapter.cadastrarCategoria(command);
		
		assertEquals(command.getCor(), categoriaDto.getCor());
		assertEquals(command.getTitulo(), categoriaDto.getTitulo());
		assertFalse(Objects.isNull(categoriaDto.getId()));
	}
	
	@Test
	@DisplayName("Tenta obter categoria pelo id")
	void obterCategoriaPeloId() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("Titulo 1", "Cor 1"));
		
		ObterCategoriaPeloIdQuery query = new ObterCategoriaPeloIdQuery(categoria.getId());
		
		CategoriaDto categoriaDto = categoriaAdapter.obterCategoriaPeloId(query);
		
		assertEquals(categoria.getId(), categoriaDto.getId());
		assertEquals(categoria.getCor(), categoriaDto.getCor());
		assertEquals(categoria.getTitulo(), categoriaDto.getTitulo());
	}
	
	@Test
	@DisplayName("Tenta obter lista de categorias")
	void obterListaDeCategorias() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("Titulo 1", "Cor 1"));
		
		Collection<CategoriaDto> listaCategorias = categoriaAdapter.obterListaDeCategorias();
		
		assertEquals(1, listaCategorias.size());
		
		CategoriaDto categoriaBanco = listaCategorias.iterator().next();
		
		assertEquals(categoria.getId(), categoriaBanco.getId());
		assertEquals(categoria.getTitulo(), categoriaBanco.getTitulo());
		assertEquals(categoria.getCor(), categoriaBanco.getCor());
	}
	
	@Test
	@DisplayName("Tenta atualizar categoria pelo id")
	void atualizarCategoriaPeloId() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("Titulo 1", "Cor 1"));
		
		AtualizarCategoriaCommand command = new AtualizarCategoriaCommand(categoria.getId(), "Novo título", "Nova Cor");
		
		CategoriaDto atualizarCategoria = categoriaAdapter.atualizarCategoria(command);
		
		assertEquals(categoria.getId(), atualizarCategoria.getId());
		assertEquals(command.getTitulo(), atualizarCategoria.getTitulo());
		assertEquals(command.getCor(), atualizarCategoria.getCor());
	}
	
	@Test
	@DisplayName("Tenta apagar categoria pelo id")
	void apagarCategoria() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("Titulo 1", "Cor 1"));
		
		ApagarCategoriaCommand command = new ApagarCategoriaCommand(categoria.getId());
		
		categoriaAdapter.apagarCategoria(command);
		
		assertTrue(Objects.isNull(testEntityManager.find(CategoriaEntity.class, categoria.getId())));
	}
	
	@Test
	@DisplayName("Tenta obter lista de videos pela categoria")
	void obterListaDeVideosPelaCategoria() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("Titulo 1", "Cor 1"));
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1", categoria));
				
		testEntityManager.flush();
		testEntityManager.clear();
		
		ObterVideosPorCategoriaQuery query = new ObterVideosPorCategoriaQuery(categoria.getId());
		
		Collection<VideoDto> videosObtidos = categoriaAdapter.obterListaDeVideosPorCategoria(query);
		
		assertEquals(1, videosObtidos.size());
		
		VideoDto videoObtido = videosObtidos.iterator().next();
		
		assertEquals(video.getCategoriaEntity().getId(), videoObtido.getCategoriaId());
		assertEquals(video.getId(), videoObtido.getId());
		assertEquals(video.getTitulo(), videoObtido.getTitulo());
		assertEquals(video.getDescricao(), videoObtido.getDescricao());
		assertEquals(video.getUrl(), videoObtido.getUrl());
	}
	
	@Test
	@DisplayName("Tenta obter categoria pelo titulo")
	void obterCategoriaPeloTitulo() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("Titulo 1", "Cor 1"));
		
		CategoriaDto categoriaObtida = categoriaAdapter.obterPeloTitulo("Titulo 1");
		
		assertEquals(categoria.getId(), categoriaObtida.getId());
		assertEquals(categoria.getTitulo(), categoriaObtida.getTitulo());
		assertEquals(categoria.getCor(), categoriaObtida.getCor());
	}
}
