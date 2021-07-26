package br.com.alura.flix.core.videos.ports.outgoing;

import java.util.Collection;
import java.util.Optional;

import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;

public interface VideosDatabase {

	Collection<VideoDto> pesquisarListaDeVideos();

	Optional<VideoDto> pesquisarPeloId(long id);

	Optional<VideoDto> deletarPeloId(Long id);

	Optional<VideoDto> cadastrarVideo(CadastrarVideoCommand command);

	Optional<VideoDto> atualizarVideo(AtualizarVideoCommand command);

}
