package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository;
import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.TokenResponse;
import lombok.extern.java.Log;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.*;
import static dev.tindersamurai.prokurator.discord.util.Helper.onError;

@Log
public class TokenClient implements DiscordTokenClient {

    private final DiscordTokenExchangeRepository repo;
    private final ExecutorService executor;

    public TokenClient(DiscordTokenExchangeRepository repo, int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        this.repo = repo;
    }

    private TokenResponse _exchange_(Map<String, String> map) throws IOException {
        val response = repo._exchange(map).execute();
        val repeat = onError(response, "Cannot exchange discord token", TokenClient.class);
        if (repeat) return _exchange_(map);

        return response.body();
    }

    @Override
    public TokenResponse exchange(TokenExchangeForm data) {
        log.info("exchange: " + data);

        val map = new HashMap<String, String>(); {
            map.put("code", data.getCode());
            map.put("grant_type", "authorization_code");
            map.put("client_id", data.getClientId());
            map.put("redirect_uri", data.getRedirectUrl());
            map.put("client_secret", data.getClientSecret());
            map.put("scope", data.getScope());
        }

        try {
            return executor.submit(() -> _exchange_(map)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenResponse refresh(TokenRefreshForm data) {
        log.info("refresh: " + data);

        val map = new HashMap<String, String>(); {
            map.put("client_id", data.getClientId());
            map.put("client_secret", data.getClientSecret());
            map.put("grant_type", "refresh_token");
            map.put("refresh_token", data.getRefreshToken());
            map.put("redirect_uri", data.getRedirectUrl());
            map.put("scope", data.getScope());
        }

        try {
            return executor.submit(() -> _exchange_(map)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
