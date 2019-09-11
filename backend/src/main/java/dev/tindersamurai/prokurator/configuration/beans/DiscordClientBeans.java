package dev.tindersamurai.prokurator.configuration.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;
import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository;
import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository;
import dev.tindersamurai.prokurator.discord.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.tindersamurai.prokurator.discord.DiscordClientFactory.*;

@Configuration
public class DiscordClientBeans {

    @Bean
    public DiscordGuildClient provideDiscordGuildClient(ObjectMapper mapper) {
        return new GuildClient(createClient(DiscordGuildRepository.class, mapper), 10);
    }

    @Bean
    public DiscordTokenClient provideDiscordTokenClient(ObjectMapper mapper) {
        return new TokenClient(createClient(DiscordTokenExchangeRepository.class, mapper), 10);
    }

    @Bean
    public DiscordUserClient provideDiscordUserClient(ObjectMapper mapper) {
        return new UserClient(createClient(DiscordUserInfoRepository.class, mapper), 10);
    }
}
