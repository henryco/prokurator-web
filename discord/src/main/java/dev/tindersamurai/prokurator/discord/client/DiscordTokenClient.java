package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.TokenResponse;

import static dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.*;

public interface DiscordTokenClient {
    TokenResponse exchange(TokenExchangeForm data);

    TokenResponse refresh(TokenRefreshForm data);
}
