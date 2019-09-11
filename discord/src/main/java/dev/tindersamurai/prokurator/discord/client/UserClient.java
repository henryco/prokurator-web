package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository;

public class UserClient implements DiscordUserClient {

    private final DiscordUserInfoRepository repo;
    public UserClient(DiscordUserInfoRepository repo) {
        this.repo = repo;
    }


}
