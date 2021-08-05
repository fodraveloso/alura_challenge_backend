package br.com.alura.flix;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alura.flix.infra.seguranca.entities.FuncaoEntity;
import br.com.alura.flix.infra.seguranca.entities.UsuarioEntity;
import br.com.alura.flix.infra.seguranca.repositories.FuncaoRepository;
import br.com.alura.flix.infra.seguranca.repositories.UsuarioRepository;
import lombok.Generated;
import lombok.RequiredArgsConstructor;

@Generated
@SpringBootApplication
@RequiredArgsConstructor
public class AluraChallengeBackendApplication implements CommandLineRunner {

	private final UsuarioRepository usuarioRepository;
	private final FuncaoRepository funcaoRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AluraChallengeBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var usuarioOwner = usuarioRepository
				.saveAndFlush(new UsuarioEntity("joao.veloso", passwordEncoder.encode("123qwe!@#")));
		FuncaoEntity funcaoOwner = funcaoRepository.saveAndFlush(new FuncaoEntity("ROLE_OWNER"));
		usuarioOwner.adicionarFuncao(funcaoOwner);
		usuarioRepository.saveAndFlush(usuarioOwner);

		UsuarioEntity usuarioAdmin = usuarioRepository
				.saveAndFlush(new UsuarioEntity("joao.fodra", passwordEncoder.encode("!@#qwe123")));
		FuncaoEntity funcaoAdmin = funcaoRepository.saveAndFlush(new FuncaoEntity("ROLE_ADMIN"));
		usuarioAdmin.adicionarFuncao(funcaoAdmin);
		usuarioRepository.saveAndFlush(usuarioAdmin);
	}
}