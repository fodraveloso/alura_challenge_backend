package br.com.alura.flix.core.models.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeletarVideoCommand {

	private final Long id;

}
