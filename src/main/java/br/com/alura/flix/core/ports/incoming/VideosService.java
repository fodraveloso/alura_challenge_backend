package br.com.alura.flix.core.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.models.query.ObterVideoQuery;

public interface VideosService {

	Collection<VideoDto> executar();

	VideoDto executar(ObterVideoQuery query);

	void executar(DeletarVideoCommand command);

	VideoDto executar(CadastrarVideoCommand command);

	VideoDto executar(AtualizarVideoCommand atualizarVideoCommand);

}
