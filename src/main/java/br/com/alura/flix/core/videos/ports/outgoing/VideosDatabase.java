package br.com.alura.flix.core.videos.ports.outgoing;

import java.util.Collection;
import java.util.Optional;

import br.com.alura.flix.core.categorias.models.command.ObterVideoPeloTituloQuery;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;

public interface VideosDatabase {

	Collection<VideoDto> pesquisarListaDeVideos();

	Optional<VideoDto> pesquisarPeloId(long id);

	void deletarPeloId(Long id);

	Optional<VideoDto> cadastrarVideo(CadastrarVideoCommand command);

	Optional<VideoDto> atualizarVideo(AtualizarVideoCommand command);

	Collection<VideoDto> obterVideoPeloTitulo(ObterVideoPeloTituloQuery query);

}
