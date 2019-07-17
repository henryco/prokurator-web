package dev.tindersamurai.prokurator.configuration.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tindersamurai.prokurator.discord.client.DiscordClientFactory;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository;
import dev.tindersamurai.prokurator.discord.client.DiscordUserInfoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordRepoBeans {

	@Bean
	public DiscordTokenExchangeRepository tokenExchangeRepository(ObjectMapper mapper) {
		return DiscordClientFactory.createClient(DiscordTokenExchangeRepository.class, mapper);
	}

	@Bean
	public DiscordUserInfoRepository userInfoRepository(ObjectMapper mapper) {
		return DiscordClientFactory.createClient(DiscordUserInfoRepository.class, mapper);
	}
}