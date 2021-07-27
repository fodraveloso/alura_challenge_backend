package br.com.alura.flix.infra.categorias.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.flix.core.categorias.models.CategoriaDto;
import br.com.alura.flix.infra.categorias.entities.CategoriaEntity;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

	Optional<CategoriaDto> findByTitulo(String titulo);
}
