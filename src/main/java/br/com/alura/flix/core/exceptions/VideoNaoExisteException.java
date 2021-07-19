package br.com.alura.flix.core.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VideoNaoExisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Long id;
}
