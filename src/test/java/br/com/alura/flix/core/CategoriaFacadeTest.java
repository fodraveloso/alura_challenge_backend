package br.com.alura.flix.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.flix.core.categorias.CategoriaFacade;
import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.core.categorias.models.command.ApagarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.AtualizarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.command.CadastrarCategoriaCommand;
import br.com.alura.flix.core.categorias.models.query.ObterCategoriaPeloIdQuery;
import br.com.alura.flix.core.categorias.ports.outgoing.CategoriaDatabase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Core: Categoria")
class CategoriaFacadeTest {

	@Mock
	private CategoriaDatabase categoriaDatabase;

	@InjectMocks
	private CategoriaFacade categoriaFacade;

	@Test
	@DisplayName("Tenta cadastrar categoria")
	void cadastrarCategoria() {

		CategoriaDto categoriaDto = new CategoriaDto(1L, "Titulo", "Cor");

		doReturn(categoriaDto).when(categoriaDatabase).cadastrarCategoria(any(CadastrarCategoriaCommand.class));

		CadastrarCategoriaCommand command = new CadastrarCategoriaCommand("Titulo", "Cor");

		CategoriaDto executar = categoriaFacade.executar(command);

		verify(categoriaDatabase, times(1)).cadastrarCategoria(any(CadastrarCategoriaCommand.class));

		assertEquals(categoriaDto.getId(), executar.getId());
		assertEquals(categoriaDto.getTitulo(), executar.getTitulo());
		assertEquals(categoriaDto.getCor(), executar.getCor());
	}

	@Test
	@DisplayName("Tenta obter categoria pelo id")
	void obterCategoriaPeloId() {

		CategoriaDto categoriaDto = new CategoriaDto(1L, "Titulo", "Cor");

		doReturn(categoriaDto).when(categoriaDatabase).obterCategoriaPeloId(any(ObterCategoriaPeloIdQuery.class));
		
		ObterCategoriaPeloIdQuery query = new ObterCategoriaPeloIdQuery(1L);
		
		CategoriaDto executar = categoriaFacade.executar(query);
		
		verify(categoriaDatabase, times(1)).obterCategoriaPeloId(any(ObterCategoriaPeloIdQuery.class));
		
		assertEquals(categoriaDto.getId(), executar.getId());
		assertEquals(categoriaDto.getTitulo(), executar.getTitulo());
		assertEquals(categoriaDto.getCor(), executar.getCor());
	}
	
	@Test
	@DisplayName("Tenta obter lista de categorias")
	void obterListaDeCategorias() {
		
		CategoriaDto categoriaDto = new CategoriaDto(1L, "Titulo", "Cor");

		doReturn(List.of(categoriaDto)).when(categoriaDatabase).obterListaDeCategorias();
		
		Collection<CategoriaDto> executarCol = categoriaFacade.executar();
		
		verify(categoriaDatabase, times(1)).obterListaDeCategorias();
		
		assertEquals(1, executarCol.size());
		
		CategoriaDto executar = executarCol.iterator().next();
		
		assertEquals(categoriaDto.getId(), executar.getId());
		assertEquals(categoriaDto.getTitulo(), executar.getTitulo());
		assertEquals(categoriaDto.getCor(), executar.getCor());
	}
	
	@Test
	@DisplayName("Tenta atualizar categoria pelo Id")
	void atualizarCategoriaPeloId() {
		
		CategoriaDto categoriaDto = new CategoriaDto(1L, "Novo Titulo", "Nova Cor");

		doReturn(categoriaDto).when(categoriaDatabase).atualizarCategoria(any(AtualizarCategoriaCommand.class));
		
		AtualizarCategoriaCommand command = new AtualizarCategoriaCommand(1L, "Novo título", "Nova Cor");
		
		CategoriaDto executar = categoriaFacade.executar(command);
		
		verify(categoriaDatabase, times(1)).atualizarCategoria(any(AtualizarCategoriaCommand.class));
		
		assertEquals(categoriaDto.getId(), executar.getId());
		assertEquals(categoriaDto.getTitulo(), executar.getTitulo());
		assertEquals(categoriaDto.getCor(), executar.getCor());
	}
	
	@Test
	@DisplayName("Tenta apagar categoria pelo id")
	void apagarCategoriaPeloId() {
		
		doNothing().when(categoriaDatabase).apagarCategoria(any(ApagarCategoriaCommand.class));
		
		ApagarCategoriaCommand command = new ApagarCategoriaCommand(1L);
		
		assertDoesNotThrow(() -> categoriaFacade.executar(command));
		
		verify(categoriaDatabase, times(1)).apagarCategoria(any(ApagarCategoriaCommand.class));
	}
}
