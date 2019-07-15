package dev.tindersamurai.prokurator.configuration.security.auth.details;

import dev.tindersamurai.prokurator.discord.client.DiscordClientFactory;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordDetailsBean {

	@Bean
	public DiscordTokenExchangeRepository tokenExchangeRepository() {
		return DiscordClientFactory.createClient(DiscordTokenExchangeRepository.class);
	}
}
