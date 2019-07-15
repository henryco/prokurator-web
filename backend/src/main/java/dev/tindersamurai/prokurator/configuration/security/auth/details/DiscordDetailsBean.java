package dev.tindersamurai.prokurator.configuration.security.auth.details;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tindersamurai.prokurator.discord.client.DiscordClientFactory;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordDetailsBean {

	@Bean
	public DiscordTokenExchangeRepository tokenExchangeRepository(ObjectMapper mapper) {
		return DiscordClientFactory.createClient(DiscordTokenExchangeRepository.class, mapper);
	}
}
