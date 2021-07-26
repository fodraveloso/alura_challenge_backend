package br.com.alura.flix.core.categorias.ports.outgoing;

import java.util.Collection;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;

public interface CategoriaDatabase {

	CategoriaDto cadastrarCategoria(CadastrarCategoriaCommand command);

	CategoriaDto obterCategoriaPeloId(ObterCategoriaPeloIdQuery query);

	Collection<CategoriaDto> obterListaDeCategorias();

	CategoriaDto atualizarCategoria(AtualizarCategoriaCommand command);

	void apagarCategoria(ApagarCategoriaCommand command);

}
