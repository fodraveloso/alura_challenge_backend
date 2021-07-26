package br.com.alura.flix.core.videos;

import java.util.Collection;

import org.springframework.stereotype.Service;

import br.com.alura.flix.core.videos.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.videos.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.videos.models.query.ObterVideoQuery;
import br.com.alura.flix.core.videos.ports.incoming.VideosService;
import br.com.alura.flix.core.videos.ports.outgoing.VideosDatabase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideosFacade implements VideosService {

	private final VideosDatabase videosDatabase;

	@Override
	public Collection<VideoDto> executar() {

		return videosDatabase.pesquisarListaDeVideos();
	}

	@Override
	public VideoDto executar(ObterVideoQuery query) {

		return videosDatabase.pesquisarPeloId(query.getId())
				.orElseThrow(() -> new VideoNaoExisteException(query.getId()));
	}

	@Override
	public void executar(DeletarVideoCommand command) {

		videosDatabase.deletarPeloId(command.getId()).orElseThrow(() -> new VideoNaoExisteException(command.getId()));
	}

	@Override
	public VideoDto executar(CadastrarVideoCommand command) {

		return videosDatabase.cadastrarVideo(command).orElseThrow();
	}

	@Override
	public VideoDto executar(AtualizarVideoCommand command) {
		
		return videosDatabase.atualizarVideo(command).orElseThrow();
	}

}
