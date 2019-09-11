package dev.tindersamurai.prokurator.configuration.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;
import dev.tindersamurai.prokurator.discord.client.DiscordGuildClient;
import dev.tindersamurai.prokurator.discord.client.GuildClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.tindersamurai.prokurator.discord.DiscordClientFactory.*;

@Configuration
public class DiscordClientBeans {

    @Bean
    public DiscordGuildClient provideDiscordGuildClient(ObjectMapper mapper) {
        return new GuildClient(createClient(DiscordGuildRepository.class, mapper), 10);
    }
}
