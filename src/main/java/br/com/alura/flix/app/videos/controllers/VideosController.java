package br.com.alura.flix.app.videos.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.app.common.models.ErroResponse;
import br.com.alura.flix.app.videos.models.AtualizarVideoRequest;
import br.com.alura.flix.app.videos.models.CadastrarVideoRequest;
import br.com.alura.flix.core.categorias.models.command.ObterVideoPeloTituloQuery;
import br.com.alura.flix.core.videos.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.videos.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.videos.models.query.ObterVideoQuery;
import br.com.alura.flix.core.videos.ports.incoming.VideosService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideosController {

	private final VideosService videosService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<VideoDto> obterListaDeVideos(@RequestParam("search") Optional<String> titulo) {

		if (titulo.isEmpty()) {
			
			return videosService.executar();
		} else {
			
			return videosService.executar(new ObterVideoPeloTituloQuery(titulo.get()));
		}
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public VideoDto obterVideoPeloId(@PathVariable("id") Long id) {

		return videosService.executar(new ObterVideoQuery(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleterVideoPeloId(@PathVariable("id") Long id) {

		videosService.executar(new DeletarVideoCommand(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarVideo(@RequestBody @Validated CadastrarVideoRequest request) {

		videosService.executar(new CadastrarVideoCommand(request.getTitulo(), request.getDescricao(), request.getUrl(),
				request.getCategoriaId()));
	}

	@PutMapping("/{id}")
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void atualizaVideo(@RequestBody @Validated AtualizarVideoRequest request, @PathVariable("id") Long id) {

		videosService
				.executar(new AtualizarVideoCommand(id, request.getTitulo(), request.getDescricao(), request.getUrl()));
	}

	@ExceptionHandler(VideoNaoExisteException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErroResponse videoNaoExiste(VideoNaoExisteException exception) {

		return new ErroResponse(HttpStatus.NOT_FOUND.value())
				.erro(String.format("Vídeo com id '%s' não existe", exception.getId()));
	}
}
