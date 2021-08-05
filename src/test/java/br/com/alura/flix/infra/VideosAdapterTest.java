package br.com.alura.flix.infra;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import br.com.alura.flix.core.categorias.models.command.ObterVideoPeloTituloQuery;
import br.com.alura.flix.core.videos.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.infra.categorias.entities.CategoriaEntity;
import br.com.alura.flix.infra.categorias.repositories.CategoriaRepository;
import br.com.alura.flix.infra.videos.VideosAdapter;
import br.com.alura.flix.infra.videos.entities.VideoEntity;
import br.com.alura.flix.infra.videos.repositories.VideoRepository;
import br.com.alura.flix.utils.TestSecurityBCryptConfig;

@DataJpaTest
@DisplayName("Infra: Videos")
@Import(TestSecurityBCryptConfig.class)
class VideosAdapterTest {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	private VideosAdapter adapter;
	
	@BeforeEach
	void setup() {
		
		this.adapter = new VideosAdapter(videoRepository, categoriaRepository);
	}
	
	@Test
	@DisplayName("Tenta pesquisar lista de vídeos")
	void pesquisarListaDeVideos() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("LIVRE", "Cor 1"));
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1", categoria));
		
		Collection<VideoDto> pesquisarListaDeVideos = adapter.pesquisarListaDeVideos();
		
		assertEquals(1, pesquisarListaDeVideos.size());
		VideoDto videoDto = pesquisarListaDeVideos.iterator().next();
		
		assertEquals(video.getId(), videoDto.getId());
		assertEquals(video.getTitulo(), videoDto.getTitulo());
		assertEquals(video.getDescricao(), videoDto.getDescricao());
		assertEquals(video.getUrl(), videoDto.getUrl());
	}
	
	@Test
	@DisplayName("Tenta pesquisa lista de vídeos vazia")
	void pesquisarListaDeVideosVazia() {
		
		Collection<VideoDto> pesquisarListaDeVideos = adapter.pesquisarListaDeVideos();
		
		assertEquals(0, pesquisarListaDeVideos.size());
	}
	
	@Test
	@DisplayName("Tenta pesquisar video pelo id")
	void pesquisarVideoPeloId() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("LIVRE", "Cor 1"));
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1", categoria));
		
		Optional<VideoDto> pesquisarPeloId = adapter.pesquisarPeloId(video.getId());
		
		assertTrue(pesquisarPeloId.isPresent());
		
		VideoDto videoDto = pesquisarPeloId.orElseGet(null);
		
		assertEquals(video.getId(), videoDto.getId());
		assertEquals(video.getTitulo(), videoDto.getTitulo());
		assertEquals(video.getDescricao(), videoDto.getDescricao());
		assertEquals(video.getUrl(), videoDto.getUrl());
	}
	
	@Test
	@DisplayName("Tenta pesquisar video pelo id que não existe")
	void pesquisaVideoPeloIdQueNaoExiste() {
		
		Optional<VideoDto> pesquisarPeloId = adapter.pesquisarPeloId(99L);
		
		assertTrue(pesquisarPeloId.isEmpty());
	}
	
	@Test
	@DisplayName("Tenta deletar video pelo id")
	void deletarVideoPeloId() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("LIVRE", "Cor 1"));
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1", categoria));
		
		assertDoesNotThrow(() -> adapter.deletarPeloId(video.getId()));
	}
	
	@Test
	@DisplayName("Tenta deletar video pelo id que não existe")
	void deletarVideoPeloIdQueNaoExiste() {
		
		assertThrows(VideoNaoExisteException.class, () -> adapter.deletarPeloId(99L));
	}
	
	@Test
	@DisplayName("Tenta cadastra video")
	void cadastrarVideo() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("LIVRE", "Cor 1"));
		
		CadastrarVideoCommand command = new CadastrarVideoCommand("Título", "Descrição", "Link", categoria.getId());
		
		Optional<VideoDto> cadastrarVideo = adapter.cadastrarVideo(command);
		
		assertTrue(cadastrarVideo.isPresent());
		
		assertEquals(command.getTitulo(), cadastrarVideo.get().getTitulo());
		assertEquals(command.getDescricao(), cadastrarVideo.get().getDescricao());
		assertEquals(command.getUrl(), cadastrarVideo.get().getUrl());
		assertNotNull(cadastrarVideo.get().getId());
	}
	
	@Test
	@DisplayName("Tenta atualizar o vídeo")
	void atualizaVideo() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("LIVRE", "Cor 1"));
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1", categoria));
		
		AtualizarVideoCommand command = new AtualizarVideoCommand(video.getId(), "Novo Título", "Nova Descrição", "Nova URL");
		
		Optional<VideoDto> atualizarVideo = adapter.atualizarVideo(command);
		
		assertTrue(atualizarVideo.isPresent());
		
		assertEquals(command.getTitulo(), atualizarVideo.get().getTitulo());
		assertEquals(command.getDescricao(), atualizarVideo.get().getDescricao());
		assertEquals(command.getUrl(), atualizarVideo.get().getUrl());
	}
	
	@Test
	@DisplayName("Tenta obter lista de videos com query")
	void obterListaDeVideosComquery() {
		
		CategoriaEntity categoria = testEntityManager.persistAndFlush(new CategoriaEntity("LIVRE", "Cor 1"));
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1", categoria));
		
		ObterVideoPeloTituloQuery query = new ObterVideoPeloTituloQuery("Titulo 1");
		
		Collection<VideoDto> videosObtido = adapter.obterVideoPeloTitulo(query);
		
		assertFalse(videosObtido.isEmpty());
		
		VideoDto videoObtido = videosObtido.iterator().next();
		
		assertEquals(video.getId(), videoObtido.getId());
		assertEquals(video.getCategoriaEntity().getId(), videoObtido.getCategoriaId());
		assertEquals(video.getTitulo(), videoObtido.getTitulo());
		assertEquals(video.getDescricao(), videoObtido.getDescricao());
		assertEquals(video.getUrl(), videoObtido.getUrl());
	}
}
