package dev.tindersamurai.prokurator.configuration.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tindersamurai.prokurator.backend.commons.client.ClientFactory;
import dev.tindersamurai.prokurator.backend.commons.service.ICollectorService;
import dev.tindersamurai.prokurator.backend.commons.service.IConfigurationService;
import dev.tindersamurai.prokurator.backend.commons.service.IFileStorageService;
import dev.tindersamurai.prokurator.backend.commons.service.IGuildService;
import dev.tindersamurai.prokurator.configuration.props.properties.ProkuratorProperties;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration @Slf4j
public class CoreBackendBeans {

	@Value static class RSCProps {
		private ObjectMapper mapper;
		private String url;
	}

	@Bean
	public RSCProps provideBackendServiceUrl(ObjectMapper mapper, ProkuratorProperties properties) {
		log.debug("provideBackendServiceUrl: {}, {}", mapper, properties);
		return new RSCProps(mapper, properties.getBackendServiceUrl());
	}

	@Bean
	public IFileStorageService provideStorageServiceClient(RSCProps p) {
		log.debug("provideStorageServiceClient: {}", p);
		return ClientFactory.createFileStorageClient(p.getUrl(), p.getMapper());
	}

	@Bean
	public IConfigurationService provideConfigurationServiceClient(RSCProps p) {
		log.debug("provideConfigurationServiceClient: {}", p);
		return ClientFactory.createConfigurationClient(p.getUrl(), p.getMapper());
	}

	@Bean
	public ICollectorService provideCollectorServiceClient(RSCProps p) {
		log.debug("provideCollectorServiceClient: {}", p);
		return ClientFactory.createCollectorClient(p.getUrl(), p.getMapper());
	}

	@Bean
	public IGuildService provideGuildServiceClient(RSCProps p) {
		log.debug("provideGuildServiceClient: {}", p);
		return ClientFactory.createGuildService(p.getUrl(), p.getMapper());
	}
}
