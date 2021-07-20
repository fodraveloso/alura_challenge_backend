package br.com.alura.flix.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.flix.infra.entities.VideoEntity;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

}
