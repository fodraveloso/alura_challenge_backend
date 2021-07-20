package br.com.alura.flix.core;

import java.util.Collection;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.alura.flix.core.exceptions.DadosParaAtualizacaoIncorretos;
import br.com.alura.flix.core.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.models.query.ObterVideoQuery;
import br.com.alura.flix.core.ports.incoming.VideosService;
import br.com.alura.flix.core.ports.outgoing.VideosDatabase;
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
		
		if (Objects.isNull(command.getTitulo()) && Objects.isNull(command.getDescricao()) && Objects.isNull(command.getUrl())) {
			
			throw new DadosParaAtualizacaoIncorretos();
		}
		
		return videosDatabase.atualizarVideo(command).orElseThrow();
	}

}
