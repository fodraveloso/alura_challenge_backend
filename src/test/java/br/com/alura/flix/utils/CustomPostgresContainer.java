package br.com.alura.flix.utils;

import java.util.Objects;

import org.testcontainers.containers.PostgreSQLContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
public class CustomPostgresContainer extends PostgreSQLContainer {

	private static final String IMAGE_VERSION = "postgres:alpine";
	private static CustomPostgresContainer container;

	private CustomPostgresContainer() {
		super(IMAGE_VERSION);
		container = this;
	}

	public static CustomPostgresContainer getInstance() {
		
		return Objects.isNull(container) ? new CustomPostgresContainer() : container;
	}

	@Override
	public void start() {
		
		super.start();
		log.debug("POSTGRES INFO");
		log.debug("DB_URL: " + container.getJdbcUrl());
		log.debug("DB_USERNAME: " + container.getUsername());
		log.debug("DB_PASSWORD: " + container.getPassword());
		System.setProperty("DB_URL", container.getJdbcUrl());
		System.setProperty("DB_USERNAME", container.getUsername());
		System.setProperty("DB_PASSWORD", container.getPassword());
	}

	@Override
	public void stop() {
		// do nothing, JVM handles shut down
	}
}