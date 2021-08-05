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
import org.springframework.context.annotation.Import;

import br.com.alura.flix.core.seguranca.models.FuncaoDto;
import br.com.alura.flix.core.seguranca.models.UsuarioDto;
import br.com.alura.flix.core.seguranca.models.command.CadastrarFuncaoCommand;
import br.com.alura.flix.core.seguranca.models.command.CadastrarUsuarioCommand;
import br.com.alura.flix.infra.seguranca.SegurancaAdapter;
import br.com.alura.flix.infra.seguranca.entities.FuncaoEntity;
import br.com.alura.flix.infra.seguranca.entities.UsuarioEntity;
import br.com.alura.flix.infra.seguranca.repositories.FuncaoRepository;
import br.com.alura.flix.infra.seguranca.repositories.UsuarioRepository;
import br.com.alura.flix.utils.TestSecurityBCryptConfig;

@DataJpaTest
@DisplayName("Infra: Segurança")
@Import(TestSecurityBCryptConfig.class)
class SegurancaAdapterTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private FuncaoRepository funcaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private SegurancaAdapter segurancaAdapter;

	@BeforeEach
	void setup() {

		this.segurancaAdapter = new SegurancaAdapter(funcaoRepository, usuarioRepository);

		this.segurancaAdapter.obterListaUsuario().forEach(
				usuario -> testEntityManager.remove(testEntityManager.find(UsuarioEntity.class, usuario.getId())));

		this.segurancaAdapter.obterListaDeFuncoes().forEach(
				funcao -> testEntityManager.remove(testEntityManager.find(FuncaoEntity.class, funcao.getId())));
		
		testEntityManager.flush();
		testEntityManager.clear();
	}

	@Test
	@DisplayName("Tenta cadastrar função")
	void cadastrarFuncao() {

		CadastrarFuncaoCommand command = new CadastrarFuncaoCommand("ROLE_OWNER");

		FuncaoDto funcaoCadastrada = segurancaAdapter.cadastrarFuncao(command);

		assertEquals(command.getNome(), funcaoCadastrada.getNome());
		assertFalse(Objects.isNull(funcaoCadastrada.getId()));
	}

	@Test
	@DisplayName("Tenta obter lista de funções")
	void obterListaDeFuncoes() {

		FuncaoEntity funcaoEntity = testEntityManager.persistAndFlush(new FuncaoEntity("ROLE_OWNER"));

		Collection<FuncaoDto> funcoesObtidas = segurancaAdapter.obterListaDeFuncoes();

		assertEquals(1, funcoesObtidas.size());

		FuncaoDto funcaoObtida = funcoesObtidas.iterator().next();

		assertEquals(funcaoEntity.getId(), funcaoObtida.getId());
		assertEquals(funcaoEntity.getNome(), funcaoObtida.getNome());
	}

	@Test
	@DisplayName("Tenta cadastrar usuario")
	void cadastrarUsuario() {

		CadastrarUsuarioCommand command = new CadastrarUsuarioCommand("joao.veloso", "123!@#qwe");

		UsuarioDto usuarioCadastrado = segurancaAdapter.cadastrarUsuario(command);

		assertEquals(command.getUsuario(), usuarioCadastrado.getUsuario());
		assertFalse(Objects.isNull(usuarioCadastrado.getId()));
	}
}
