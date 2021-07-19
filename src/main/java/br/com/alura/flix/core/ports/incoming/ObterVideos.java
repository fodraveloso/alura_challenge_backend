package br.com.alura.flix.core.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.query.ObterVideoQuery;

public interface ObterVideos {

	Collection<VideoDto> executar();

	VideoDto executar(ObterVideoQuery query);

}
