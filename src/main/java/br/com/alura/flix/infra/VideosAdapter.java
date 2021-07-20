package br.com.alura.flix.infra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.alura.flix.core.models.VideoDto;
import br.com.alura.flix.core.models.command.AtualizarVideoCommand;
import br.com.alura.flix.core.models.command.CadastrarVideoCommand;
import br.com.alura.flix.core.ports.outgoing.VideosDatabase;
import br.com.alura.flix.infra.entities.VideoEntity;
import br.com.alura.flix.infra.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideosAdapter implements VideosDatabase {

	private final VideoRepository videoRepository;

	@Override
	public Collection<VideoDto> pesquisarListaDeVideos() {

		return videoRepository.findAll().stream()
				.map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Optional<VideoDto> pesquisarPeloId(long id) {

		return videoRepository.findById(id)
				.map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl()));
	}

	@Override
	public Optional<VideoDto> deletarPeloId(Long id) {

		Optional<VideoEntity> videoOptional = videoRepository.findById(id);
		if (videoOptional.isPresent()) {

			videoRepository.delete(videoOptional.get());
		}
		return videoOptional
				.map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl()));
	}

	@Override
	public Optional<VideoDto> cadastrarVideo(CadastrarVideoCommand command) {

		return Optional
				.of(videoRepository
						.save(new VideoEntity(command.getTitulo(), command.getDescricao(), command.getUrl())))
				.map(video -> new VideoDto(video.getId(), video.getTitulo(), video.getDescricao(), video.getUrl()));
	}

	@Override
	public Optional<VideoDto> atualizarVideo(AtualizarVideoCommand command) {

		VideoEntity video = videoRepository.findById(command.getId()).orElseThrow();

		Optional.ofNullable(command.getTitulo()).ifPresent(video::atualizarTitulo);
		Optional.ofNullable(command.getDescricao()).ifPresent(video::atualizarDescricao);
		Optional.ofNullable(command.getUrl()).ifPresent(video::atualizarUrl);

		return Optional.of(videoRepository.save(video))
				.map(v -> new VideoDto(v.getId(), v.getTitulo(), v.getDescricao(), v.getUrl()));
	}
}
