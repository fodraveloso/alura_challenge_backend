package br.com.alura.flix.core.videos.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.videos.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.videos.models.query.ObterVideoQuery;

public interface VideosService {

	Collection<VideoDto> executar();

	VideoDto executar(ObterVideoQuery query);

	void executar(DeletarVideoCommand command);

	VideoDto executar(CadastrarVideoCommand command);

	VideoDto executar(AtualizarVideoCommand atualizarVideoCommand);

}
