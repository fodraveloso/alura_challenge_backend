package br.com.alura.flix.core.categorias;

import java.util.Collection;

import org.springframework.stereotype.Service;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.models.query.ObterVideosPorCategoriaQuery;
import br.com.alura.flix.core.categorias.ports.incoming.CategoriaService;
import br.com.alura.flix.core.categorias.ports.outgoing.CategoriaDatabase;
import br.com.alura.flix.core.videos.models.VideoDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaFacade implements CategoriaService {

	private final CategoriaDatabase categoriaDatabase;
	
	@Override
	public CategoriaDto executar(CadastrarCategoriaCommand command) {

		return categoriaDatabase.cadastrarCategoria(command);
	}

	@Override
	public CategoriaDto executar(ObterCategoriaPeloIdQuery query) {

		return categoriaDatabase.obterCategoriaPeloId(query);
	}

	@Override
	public Collection<CategoriaDto> executar() {

		return categoriaDatabase.obterListaDeCategorias();
	}

	@Override
	public CategoriaDto executar(AtualizarCategoriaCommand command) {

		return categoriaDatabase.atualizarCategoria(command);
	}

	@Override
	public void executar(ApagarCategoriaCommand command) {

		categoriaDatabase.apagarCategoria(command);
	}

	@Override
	public Collection<VideoDto> executar(ObterVideosPorCategoriaQuery query) {

		return categoriaDatabase.obterListaDeVideosPorCategoria(query);
	}
}
