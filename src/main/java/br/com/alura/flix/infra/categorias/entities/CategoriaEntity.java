package br.com.alura.flix.infra.categorias.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.alura.flix.infra.videos.entities.VideoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Categoria")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CategoriaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String titulo;
	
	@Column(nullable = false)
	private String cor;
	
	@OneToMany(mappedBy = "categoriaEntity")
	private List<VideoEntity> videos;
	

	public CategoriaEntity(String titulo, String cor) {
		this.titulo = titulo;
		this.cor = cor;
	}
	
	public void atualizarTitulo(String titulo) {
		
		this.titulo = titulo;
	}
	
	public void atualizarCor(String cor) {
		
		this.cor = cor;
	}
}