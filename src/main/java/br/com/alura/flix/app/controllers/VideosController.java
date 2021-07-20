package br.com.alura.flix.app.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.app.models.AtualizarVideoRequest;
import br.com.alura.flix.app.models.CadastrarVideoRequest;
import br.com.alura.flix.app.models.ErroResponse;
import br.com.alura.flix.core.exceptions.DadosParaAtualizacaoIncorretos;
import br.com.alura.flix.core.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.models.command.DeletarVideoCommand;
import br.com.alura.flix.core.models.query.ObterVideoQuery;
import br.com.alura.flix.core.ports.incoming.VideosService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideosController {

	private final VideosService videosService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<VideoDto> obterListaDeVideos() {

		return videosService.executar();
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

		videosService
				.executar(new CadastrarVideoCommand(request.getTitulo(), request.getDescricao(), request.getUrl()));
	}

	@PutMapping("/{id}")
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void atualizaVideo(@RequestBody AtualizarVideoRequest request, @PathVariable("id") Long id) {

		videosService
				.executar(new AtualizarVideoCommand(id, request.getTitulo(), request.getDescricao(), request.getUrl()));
	}

	@ExceptionHandler(VideoNaoExisteException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErroResponse videoNaoExiste(VideoNaoExisteException exception) {

		return new ErroResponse(HttpStatus.NOT_FOUND.value())
				.erro(String.format("Vídeo com id '%s' não existe", exception.getId()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErroResponse validacaoPayload(MethodArgumentNotValidException exception) {

		var erroResponse = new ErroResponse(HttpStatus.BAD_REQUEST.value());
		erroResponse.getErros().addAll(exception.getAllErrors().stream().map(FieldError.class::cast)
				.map(FieldError::getDefaultMessage).collect(Collectors.toCollection(ArrayList::new)));
		return erroResponse;
	}

	@ExceptionHandler(DadosParaAtualizacaoIncorretos.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErroResponse dadosParaAtualizacaoIncorretos(DadosParaAtualizacaoIncorretos exception) {

		return new ErroResponse(HttpStatus.BAD_REQUEST.value()).erro("Dados para atualização inválidos");
	}
}
