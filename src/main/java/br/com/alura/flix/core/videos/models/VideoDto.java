package br.com.alura.flix.core.videos.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VideoDto {

	private final Long id;
	private final String titulo;
	private final String descricao;
	private final String url;
}
