package br.com.alura.flix.infra.seguranca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.flix.infra.seguranca.entities.FuncaoEntity;

public interface FuncaoRepository extends JpaRepository<FuncaoEntity, Long> {

}
