package br.com.alura.flix.infra.seguranca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.flix.infra.seguranca.entities.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

	Optional<UsuarioEntity> findByUsername(String username);
}
