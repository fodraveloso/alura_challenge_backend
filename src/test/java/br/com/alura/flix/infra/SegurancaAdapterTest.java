package br.com.alura.flix.infra;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.infra.seguranca.FuncaoAdapter;
import br.com.alura.flix.infra.seguranca.entities.FuncaoEntity;
import br.com.alura.flix.infra.seguranca.repositories.FuncaoRepository;

@DataJpaTest
@DisplayName("Infra: Segurança")
class SegurancaAdapterTest {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private FuncaoRepository funcaoRepository;
	
	private FuncaoAdapter funcaoAdapter;
	
	@BeforeEach
	void setup() {
		
		this.funcaoAdapter = new FuncaoAdapter(funcaoRepository);
	}
	
	@Test
	@DisplayName("Tenta cadastrar função") 
	void cadastrarFuncao() {
		
		CadastrarFuncaoCommand command = new CadastrarFuncaoCommand("ROLE_OWNER");
		
		FuncaoDto funcaoCadastrada = funcaoAdapter.cadastrarFuncao(command);
		
		assertEquals(command.getNome(), funcaoCadastrada.getNome());
		assertFalse(Objects.isNull(funcaoCadastrada.getId()));
	}
	
	@Test
	@DisplayName("Tenta obter lista de funções")
	void obterListaDeFuncoes() {
		
		FuncaoEntity funcaoEntity = testEntityManager.persistAndFlush(new FuncaoEntity("ROLE_OWNER"));
		
		Collection<FuncaoDto> funcoesObtidas = funcaoAdapter.obterListaDeFuncoes();
		
		assertEquals(1, funcoesObtidas.size());
		
		FuncaoDto funcaoObtida = funcoesObtidas.iterator().next();
		
		assertEquals(funcaoEntity.getId(), funcaoObtida.getId());
		assertEquals(funcaoEntity.getNome(), funcaoObtida.getNome());
	}
}

