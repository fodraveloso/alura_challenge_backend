package br.com.alura.flix.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.models.command.CadastrarVideoCommand;
import br.com.alura.flix.infra.entities.VideoEntity;
import br.com.alura.flix.infra.repositories.VideoRepository;

@DataJpaTest
@DisplayName("Infra: Videos")
class VideosAdapterTest {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private VideoRepository videoRepository;
	
	private VideosAdapter adapter;
	
	@BeforeEach
	void setup() {
		
		this.adapter = new VideosAdapter(videoRepository);
	}
	
	@Test
	@DisplayName("Tenta pesquisar lista de vídeos")
	void pesquisarListaDeVideos() {
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1"));
		
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
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1"));
		
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
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1"));
		
		Optional<VideoDto> deletarPeloId = adapter.deletarPeloId(video.getId());
		
		assertTrue(deletarPeloId.isPresent());
	}
	
	@Test
	@DisplayName("Tenta deletar video pelo id que não existe")
	void deletarVideoPeloIdQUeNaoExiste() {
		
		Optional<VideoDto> deletarPeloId = adapter.deletarPeloId(1L);
		
		assertTrue(deletarPeloId.isEmpty());
	}
	
	@Test
	@DisplayName("Tenta cadastra video")
	void cadastrarVideo() {
		
		CadastrarVideoCommand command = new CadastrarVideoCommand("Título", "Descrição", "Link");
		
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
		
		VideoEntity video = testEntityManager.persistAndFlush(new VideoEntity("Titulo 1", "Descrição 1", "Link 1"));
		
		AtualizarVideoCommand command = new AtualizarVideoCommand(video.getId(), "Novo Título", "Nova Descrição", "Nova URL");
		
		Optional<VideoDto> atualizarVideo = adapter.atualizarVideo(command);
		
		assertTrue(atualizarVideo.isPresent());
		
		assertEquals(command.getTitulo(), atualizarVideo.get().getTitulo());
		assertEquals(command.getDescricao(), atualizarVideo.get().getDescricao());
		assertEquals(command.getUrl(), atualizarVideo.get().getUrl());
	}
}
