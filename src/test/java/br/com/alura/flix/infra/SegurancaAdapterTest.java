package br.com.alura.flix.infra;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.alura.flix.app.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.infra.seguranca.SegurancaAdapter;
import br.com.alura.flix.infra.seguranca.repositories.FuncaoRepository;

@DataJpaTest
@DisplayName("Infra: Segurança")
class SegurancaAdapterTest {

	@Autowired
	private FuncaoRepository funcaoRepository;
	
	private SegurancaAdapter segurancaAdapater;
	
	@BeforeEach
	void setup() {
		
		this.segurancaAdapater = new SegurancaAdapter(funcaoRepository);
	}
	
	@Test
	@DisplayName("Tenta cadastrar função") 
	void cadastrarFuncao() {
		
		CadastrarFuncaoCommand command = new CadastrarFuncaoCommand("ROLE_OWNER");
		
		FuncaoDto funcaoCadastrada = segurancaAdapater.cadastrarFuncao(command);
		
		assertEquals(command.getNome(), funcaoCadastrada.getNome());
		assertFalse(Objects.isNull(funcaoCadastrada.getId()));
	}
}
