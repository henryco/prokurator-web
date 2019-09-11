package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository;

public class TokenClient implements DiscordTokenClient {

    private final DiscordTokenExchangeRepository repo;
    public TokenClient(DiscordTokenExchangeRepository repo) {
        this.repo = repo;
    }
}
