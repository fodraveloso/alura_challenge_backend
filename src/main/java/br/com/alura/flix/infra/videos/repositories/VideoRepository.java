package br.com.alura.flix.infra.videos.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.flix.infra.videos.entities.VideoEntity;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

	Collection<VideoEntity> findByTitulo(String titulo);

}
