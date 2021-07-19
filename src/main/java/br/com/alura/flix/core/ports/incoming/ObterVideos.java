package br.com.alura.flix.core.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.models.VideoDto;

public interface ObterVideos {

	Collection<VideoDto> executar();

}
