package br.com.alura.flix.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.flix.core.videos.VideosFacade;
import br.com.alura.flix.core.videos.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.videos.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.videos.models.query.ObterVideoQuery;
import br.com.alura.flix.core.videos.ports.outgoing.VideosDatabase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Core: Videos")
class VideosFacadeTest {

	@Mock
	private VideosDatabase videosDatabase;

	@InjectMocks
	private VideosFacade videosFacade;

	@Test
	@DisplayName("Tenta pesquisar lista de vídeos")
	void pesquisarListaDeVideos_sucesso() {

		VideoDto videoDto = new VideoDto(1L, "Titulo", "Descrição", "Link do vídeo");

		doReturn(List.of(videoDto)).when(videosDatabase).pesquisarListaDeVideos();

		Collection<VideoDto> listaDeVideos = videosFacade.executar();

		verify(videosDatabase, times(1)).pesquisarListaDeVideos();

		assertEquals(videoDto, listaDeVideos.iterator().next());
	}

	@Test
	@DisplayName("Tenta pesquisar lista de vídeos vazia")
	void pesquisaListaDeVideosvazia() {

		doReturn(List.of()).when(videosDatabase).pesquisarListaDeVideos();

		Collection<VideoDto> listaDeVideos = videosFacade.executar();

		verify(videosDatabase, times(1)).pesquisarListaDeVideos();

		assertTrue(listaDeVideos.isEmpty());
	}

	@Test
	@DisplayName("Tenta pesquisar video pelo id")
	void pesquisarVideoPeloId() {

		VideoDto videoDto = new VideoDto(1L, "Titulo", "Descrição", "Link do vídeo");

		doReturn(Optional.of(videoDto)).when(videosDatabase).pesquisarPeloId(1L);

		VideoDto videoDtoRecuperado = videosFacade.executar(new ObterVideoQuery(1L));

		verify(videosDatabase, times(1)).pesquisarPeloId(1L);

		assertEquals(videoDto, videoDtoRecuperado);
	}

	@Test
	@DisplayName("Tenta pesquisar video pelo id que não existe")
	void pesquisarVideoPeloIdQueNaoExiste() {

		doReturn(Optional.empty()).when(videosDatabase).pesquisarPeloId(1L);

		ObterVideoQuery obterVideoQuery = new ObterVideoQuery(1L);

		assertThrows(VideoNaoExisteException.class, () -> videosFacade.executar(obterVideoQuery));

		verify(videosDatabase, times(1)).pesquisarPeloId(1L);
	}

	@Test
	@DisplayName("Tenta deletar video pelo id")
	void deltarVideoPeloId() {

		VideoDto videoDto = new VideoDto(1L, "Titulo", "Descrição", "Link do vídeo");

		doReturn(Optional.of(videoDto)).when(videosDatabase).deletarPeloId(1L);

		DeletarVideoCommand command = new DeletarVideoCommand(1L);

		assertDoesNotThrow(() -> videosFacade.executar(command));

		verify(videosDatabase, times(1)).deletarPeloId(1L);
	}

	@Test
	@DisplayName("Tenta deletar video pelo id que não existe")
	void deletarVideoPeloIdQueNaoExiste() {

		doReturn(Optional.empty()).when(videosDatabase).deletarPeloId(1L);

		DeletarVideoCommand command = new DeletarVideoCommand(1L);

		assertThrows(VideoNaoExisteException.class, () -> videosFacade.executar(command));

		verify(videosDatabase, times(1)).deletarPeloId(1L);
	}

	@Test
	@DisplayName("Tenta cadastrar video")
	void cadastrarVideo() {

		VideoDto videoDto = new VideoDto(1L, "Título", "Descrição", "Link");

		doReturn(Optional.of(videoDto)).when(videosDatabase).cadastrarVideo(Mockito.any(CadastrarVideoCommand.class));
		
		CadastrarVideoCommand command = new CadastrarVideoCommand("Título", "Descrição", "Link");
		
		VideoDto videoPersistido = videosFacade.executar(command);
		
		assertEquals(videoDto.getId(), videoPersistido.getId());
		assertEquals(videoDto.getTitulo(), videoPersistido.getTitulo());
		assertEquals(videoDto.getDescricao(), videoPersistido.getDescricao());
		assertEquals(videoDto.getUrl(), videoPersistido.getUrl());
	}
	
	@Test
	@DisplayName("Tenta atualizar video")
	void atualizarVideo() {
		
		VideoDto videoDto = new VideoDto(1L, "Novo Título", "Descrição", "Link");
		
		doReturn(Optional.of(videoDto)).when(videosDatabase).atualizarVideo(Mockito.any(AtualizarVideoCommand.class));
		
		AtualizarVideoCommand command = new AtualizarVideoCommand(1L, "Novo Título", null, null);
		
		VideoDto videoAtualizado = videosFacade.executar(command);
		
		assertEquals(videoDto.getId(), videoAtualizado.getId());
		assertEquals(videoDto.getTitulo(), videoAtualizado.getTitulo());
		assertEquals(videoDto.getDescricao(), videoAtualizado.getDescricao());
		assertEquals(videoDto.getUrl(), videoAtualizado.getUrl());
	}
}