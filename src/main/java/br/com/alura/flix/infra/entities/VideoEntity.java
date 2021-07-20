package br.com.alura.flix.infra.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

	public VideoEntity(String titulo, String descricao, String url) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.url = url;
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