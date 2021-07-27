package br.com.alura.flix.infra.videos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.alura.flix.core.categorias.models.command.ObterVideoPeloTituloQuery;
import br.com.alura.flix.core.videos.exceptions.VideoNaoExisteException;
import br.com.alura.flix.core.videos.models.VideoDto;
import br.com.alura.flix.core.videos.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.videos.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.videos.ports.outgoing.VideosDatabase;
import br.com.alura.flix.infra.categorias.entities.CategoriaEntity;
import br.com.alura.flix.infra.categorias.repositories.CategoriaRepository;
import br.com.alura.flix.infra.videos.entities.VideoEntity;
import br.com.alura.flix.infra.videos.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideosAdapter implements VideosDatabase {

	private final VideoRepository videoRepository;
	private final CategoriaRepository categoriaRepository;

	@Override
	public Collection<VideoDto> pesquisarListaDeVideos() {

		return videoRepository
				.findAll().stream().map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(),
						video.getUrl(), video.getCategoriaEntity().getId()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Optional<VideoDto> pesquisarPeloId(long id) {

		return videoRepository.findById(id).map(video -> new VideoDto(video.getId(), video.getTitulo(),
				video.getDescricao(), video.getUrl(), video.getCategoriaEntity().getId()));
	}

	@Override
	public void deletarPeloId(Long id) {

		videoRepository.findById(id).ifPresentOrElse(videoRepository::delete, () -> new VideoNaoExisteException(id));
	}

	@Override
	public Optional<VideoDto> cadastrarVideo(CadastrarVideoCommand command) {

		CategoriaEntity categoria = categoriaRepository.findById(command.getCategoriaId()).orElseThrow();

		return Optional
				.of(videoRepository.save(
						new VideoEntity(command.getTitulo(), command.getDescricao(), command.getUrl(), categoria)))
				.map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl(),
						video.getId()));
	}

	@Override
	public Optional<VideoDto> atualizarVideo(AtualizarVideoCommand command) {

		VideoEntity video = videoRepository.findById(command.getId()).orElseThrow();

		Optional.ofNullable(command.getTitulo()).ifPresent(video::atualizarTitulo);
		Optional.ofNullable(command.getDescricao()).ifPresent(video::atualizarDescricao);
		Optional.ofNullable(command.getUrl()).ifPresent(video::atualizarUrl);

		return Optional.of(videoRepository.save(video))
				.map(v -> new VideoDto(v.getId(), v.getTitulo(), v.getDescricao(), v.getUrl(), v.getId()));
	}

	@Override
	public Collection<VideoDto> obterVideoPeloTitulo(ObterVideoPeloTituloQuery query) {

		return videoRepository
				.findByTitulo(query.getTitulo()).stream().map(video -> new VideoDto(video.getId(), video.getTitulo(),
						video.getDescricao(), video.getUrl(), video.getCategoriaEntity().getId()))
				.collect(Collectors.toList());
	}
}
