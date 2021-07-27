package br.com.alura.flix.core.videos.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoDto {

	private Long id;
	private String titulo;
	private String descricao;
	private String url;
	private Long categoriaId;
}
