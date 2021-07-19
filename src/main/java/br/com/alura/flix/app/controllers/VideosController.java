package br.com.alura.flix.app.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.app.models.ErroResponse;
import br.com.alura.flix.core.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.query.ObterVideoQuery;
import br.com.alura.flix.core.ports.incoming.ObterVideos;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideosController {

	private final ObterVideos obterVideos;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<VideoDto> obterListaDeVideos() {

		return obterVideos.executar();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public VideoDto obterVideoPeloId(@PathVariable("id") Long id) {

		return obterVideos.executar(new ObterVideoQuery(id));
	}

	@ExceptionHandler(VideoNaoExisteException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErroResponse videoNaoExiste(VideoNaoExisteException exception) {

		return new ErroResponse(HttpStatus.BAD_REQUEST.value())
				.erro(String.format("Vídeo com id '%s' não existe", exception.getId()));
	}
}
