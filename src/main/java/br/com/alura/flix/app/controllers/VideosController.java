package br.com.alura.flix.app.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.core.models.VideoDto;
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
}
