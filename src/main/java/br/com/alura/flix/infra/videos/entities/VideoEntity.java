package br.com.alura.flix.infra.videos.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.alura.flix.infra.categorias.entities.CategoriaEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Videos")
@NoArgsConstructor
public class VideoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String titulo;
	
	@Column(nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	private String url;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	private CategoriaEntity categoriaEntity;
	

	public VideoEntity(String titulo, String descricao, String url, CategoriaEntity categoriaEntity) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.url = url;
		this.categoriaEntity = categoriaEntity;
	}

	public void atualizarTitulo(String titulo) {
		
		this.titulo = titulo;
	}
	
	public void atualizarDescricao(String descricao) {
		
		this.descricao = descricao;
	}
	
	public void atualizarUrl(String url) {
		
		this.url = url;
	}
}