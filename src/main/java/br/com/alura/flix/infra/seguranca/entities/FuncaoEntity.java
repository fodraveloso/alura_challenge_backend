package br.com.alura.flix.infra.seguranca.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Funcao")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class FuncaoEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String nome;

	public FuncaoEntity(String nome) {
		this.nome = nome;
	}
}