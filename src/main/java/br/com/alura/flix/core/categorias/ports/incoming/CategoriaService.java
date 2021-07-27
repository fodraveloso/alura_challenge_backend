package br.com.alura.flix.core.categorias.ports.incoming;

import java.util.Collection;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.models.query.ObterVideosPorCategoriaQuery;
import br.com.alura.flix.core.videos.models.VideoDto;

public interface CategoriaService {

	CategoriaDto executar(CadastrarCategoriaCommand command);

	CategoriaDto executar(ObterCategoriaPeloIdQuery query);

	Collection<CategoriaDto> executar();

	CategoriaDto executar(AtualizarCategoriaCommand command);

	void executar(ApagarCategoriaCommand command);

	Collection<VideoDto> executar(ObterVideosPorCategoriaQuery query);
}
