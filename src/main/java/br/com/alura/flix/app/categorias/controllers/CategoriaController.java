package br.com.alura.flix.app.categorias.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.flix.app.categorias.models.AtualizarCategoriaRequest;
import br.com.alura.flix.app.categorias.models.CadastrarCategoriaRequest;
import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.models.query.ObterVideosPorCategoriaQuery;
import br.com.alura.flix.core.categorias.ports.incoming.CategoriaService;
import br.com.alura.flix.core.videos.models.VideoDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categorias")
public class CategoriaController {

	private final CategoriaService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Secured({"ROLE_ADMIN"})
	public CategoriaDto cadastrarCategoria(@RequestBody @Validated CadastrarCategoriaRequest request) {

		return service.executar(new CadastrarCategoriaCommand(request.getTitulo(), request.getCor()));
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN","ROLE_OWNER"})
	public CategoriaDto obterCategoriaPeloId(@PathVariable("id") Long id) {

		return service.executar(new ObterCategoriaPeloIdQuery(id));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN"})
	public Collection<CategoriaDto> listarCategorias() {

		return service.executar();
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN"})
	public CategoriaDto atualizarCategoria(@PathVariable("id") Long id,
			@RequestBody @Validated AtualizarCategoriaRequest request) {

		return service.executar(new AtualizarCategoriaCommand(id, request.getTitulo(), request.getCor()));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN"})
	public void apagarCategoriaPeloId(@PathVariable("id") Long id) {
		
		service.executar(new ApagarCategoriaCommand(id));
	}
	
	@GetMapping("/{id}/videos")
	@ResponseStatus(HttpStatus.OK)
	@Secured({"ROLE_ADMIN","ROLE_OWNER"})
	public Collection<VideoDto> obterListaDeVideosPorCategoria(@PathVariable("id") Long id) {
		
		return service.executar(new ObterVideosPorCategoriaQuery(id));
	}
}
