package br.com.alura.flix.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ObterVideoPeloTituloQuery;
import br.com.alura.flix.core.categorias.ports.outgoing.CategoriaDatabase;
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
	
	@Mock
	private CategoriaDatabase categoriaDatabase;

	@InjectMocks
	private VideosFacade videosFacade;

	@Test
	@DisplayName("Tenta pesquisar lista de vídeos")
	void pesquisarListaDeVideos_sucesso() {

		VideoDto videoDto = new VideoDto(1L, "Titulo", "Descrição", "Link do vídeo", 1L);

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

		VideoDto videoDto = new VideoDto(1L, "Titulo", "Descrição", "Link do vídeo", 1L);

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

		doNothing().when(videosDatabase).deletarPeloId(1L);

		DeletarVideoCommand command = new DeletarVideoCommand(1L);

		assertDoesNotThrow(() -> videosFacade.executar(command));

		verify(videosDatabase, times(1)).deletarPeloId(1L);
	}

	@Test
	@DisplayName("Tenta deletar video pelo id que não existe")
	void deletarVideoPeloIdQueNaoExiste() {

		doThrow(new VideoNaoExisteException(1L)).when(videosDatabase).deletarPeloId(1L);

		DeletarVideoCommand command = new DeletarVideoCommand(1L);

		assertThrows(VideoNaoExisteException.class, () -> videosFacade.executar(command));
		
		verify(videosDatabase, times(1)).deletarPeloId(1L);
	}

	@Test
	@DisplayName("Tenta cadastrar video")
	void cadastrarVideo() {

		VideoDto videoDto = new VideoDto(1L, "Título", "Descrição", "Link", 1L);

		doReturn(Optional.of(videoDto)).when(videosDatabase).cadastrarVideo(any(CadastrarVideoCommand.class));
		
		CadastrarVideoCommand command = new CadastrarVideoCommand("Título", "Descrição", "Link", 1L);
		
		VideoDto videoPersistido = videosFacade.executar(command);
		
		verify(videosDatabase, times(1)).cadastrarVideo(any(CadastrarVideoCommand.class));
		
		assertEquals(videoDto.getId(), videoPersistido.getId());
		assertEquals(videoDto.getTitulo(), videoPersistido.getTitulo());
		assertEquals(videoDto.getDescricao(), videoPersistido.getDescricao());
		assertEquals(videoDto.getUrl(), videoPersistido.getUrl());
	}
	
	@Test
	@DisplayName("Tenta cadastrar vídeo sem categoria")
	void cadastrarVideoSemCategoria() {
		
		VideoDto videoDto = new VideoDto(1L, "Título", "Descrição", "Link", 1L);
		CategoriaDto categoriaDto = new CategoriaDto(1L, "LIVRE", "Azul");
		
		doReturn(Optional.of(videoDto)).when(videosDatabase).cadastrarVideo(any(CadastrarVideoCommand.class));
		doReturn(categoriaDto).when(categoriaDatabase).obterPeloTitulo(any(String.class));
		
		CadastrarVideoCommand command = new CadastrarVideoCommand("Título", "Descrição", "Link", null);
		
		VideoDto videoPersistido = videosFacade.executar(command);
		
		verify(videosDatabase, times(1)).cadastrarVideo(any(CadastrarVideoCommand.class));
		
		assertEquals(videoDto.getId(), videoPersistido.getId());
		assertEquals(videoDto.getTitulo(), videoPersistido.getTitulo());
		assertEquals(videoDto.getDescricao(), videoPersistido.getDescricao());
		assertEquals(videoDto.getUrl(), videoPersistido.getUrl());
	}
	
	@Test
	@DisplayName("Tenta atualizar video")
	void atualizarVideo() {
		
		VideoDto videoDto = new VideoDto(1L, "Novo Título", "Descrição", "Link", 1L);
		
		doReturn(Optional.of(videoDto)).when(videosDatabase).atualizarVideo(any(AtualizarVideoCommand.class));
		
		AtualizarVideoCommand command = new AtualizarVideoCommand(1L, "Novo Título", null, null);
		
		VideoDto videoAtualizado = videosFacade.executar(command);
		
		verify(videosDatabase, times(1)).atualizarVideo(any(AtualizarVideoCommand.class));
		
		assertEquals(videoDto.getId(), videoAtualizado.getId());
		assertEquals(videoDto.getTitulo(), videoAtualizado.getTitulo());
		assertEquals(videoDto.getDescricao(), videoAtualizado.getDescricao());
		assertEquals(videoDto.getUrl(), videoAtualizado.getUrl());
	}
	
	@Test
	@DisplayName("Tenta obter videos com query")
	void obterVideoComQuery() {
		
		VideoDto videoDto = new VideoDto(1L, "Novo Título", "Descrição", "Link", 1L);
		
		doReturn(List.of(videoDto)).when(videosDatabase).obterVideoPeloTitulo(any(ObterVideoPeloTituloQuery.class));
		
		ObterVideoPeloTituloQuery query = new ObterVideoPeloTituloQuery("Novo Título");
		
		Collection<VideoDto> executar = videosFacade.executar(query);
		
		verify(videosDatabase, times(1)).obterVideoPeloTitulo(any(ObterVideoPeloTituloQuery.class));
		
		assertEquals(1, executar.size());
		
		VideoDto video = executar.iterator().next();
		
		assertEquals(videoDto.getId(), video.getId());
		assertEquals(videoDto.getCategoriaId(), video.getId());
		assertEquals(videoDto.getTitulo(), video.getTitulo());
		assertEquals(videoDto.getDescricao(), video.getDescricao());
		assertEquals(videoDto.getUrl(), video.getUrl());
	}
}
