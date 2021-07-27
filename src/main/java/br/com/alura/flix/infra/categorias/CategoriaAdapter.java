package br.com.alura.flix.infra.categorias;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.models.query.ObterVideosPorCategoriaQuery;
import br.com.alura.flix.core.categorias.ports.outgoing.CategoriaDatabase;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.infra.categorias.entities.CategoriaEntity;
import br.com.alura.flix.infra.categorias.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoriaAdapter implements CategoriaDatabase {

	private final CategoriaRepository categoriaRepository;

	@Override
	public CategoriaDto cadastrarCategoria(CadastrarCategoriaCommand command) {

		return Optional.of(categoriaRepository.save(new CategoriaEntity(command.getTitulo(), command.getCor())))
				.map(categoria -> new CategoriaDto(categoria.getId(), categoria.getTitulo(), categoria.getCor()))
				.orElseThrow();
	}

	@Override
	public CategoriaDto obterCategoriaPeloId(ObterCategoriaPeloIdQuery query) {

		return categoriaRepository.findById(query.getId())
				.map(categoria -> new CategoriaDto(categoria.getId(), categoria.getTitulo(), categoria.getCor()))
				.orElseThrow();
	}

	@Override
	public Collection<CategoriaDto> obterListaDeCategorias() {

		return categoriaRepository.findAll().stream()
				.map(categoria -> new CategoriaDto(categoria.getId(), categoria.getTitulo(), categoria.getCor()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public CategoriaDto atualizarCategoria(AtualizarCategoriaCommand command) {

		CategoriaEntity categoria = categoriaRepository.findById(command.getId()).orElseThrow();

		Optional.ofNullable(command.getCor()).ifPresent(categoria::atualizarCor);
		Optional.ofNullable(command.getTitulo()).ifPresent(categoria::atualizarTitulo);

		return Optional.of(categoriaRepository.save(categoria))
				.map(cat -> new CategoriaDto(cat.getId(), cat.getTitulo(), cat.getCor())).orElseThrow();
	}

	@Override
	public void apagarCategoria(ApagarCategoriaCommand command) {

		CategoriaEntity categoria = categoriaRepository.findById(command.getId()).orElseThrow();
		categoriaRepository.delete(categoria);
	}

	@Override
	public CategoriaDto obterPeloTitulo(String titulo) {

		return categoriaRepository.findByTitulo(titulo)
				.map(categoria -> new CategoriaDto(categoria.getId(), categoria.getTitulo(), categoria.getCor()))
				.orElseThrow();
	}

	@Override
	public Collection<VideoDto> obterListaDeVideosPorCategoria(ObterVideosPorCategoriaQuery query) {

		return categoriaRepository.findById(query.getCategoriaId()).orElseThrow().getVideos().stream()
				.map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl(),
						query.getCategoriaId())).collect(Collectors.toCollection(ArrayList::new));
	}
}