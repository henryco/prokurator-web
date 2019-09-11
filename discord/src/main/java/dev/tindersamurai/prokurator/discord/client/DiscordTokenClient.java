package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository;

public interface DiscordTokenClient {
    DiscordTokenExchangeRepository.TokenResponse exchange(DiscordTokenExchangeRepository.TokenExchangeForm data);

    DiscordTokenExchangeRepository.TokenResponse refresh(DiscordTokenExchangeRepository.TokenRefreshForm data);
}
