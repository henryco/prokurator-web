package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository;
import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository.GuildsResponse;
import dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository.UserResponse;
import lombok.extern.java.Log;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.tindersamurai.prokurator.discord.util.Helper.onError;

@Log
public class UserClient implements DiscordUserClient {

    private final DiscordUserInfoRepository repo;
    private final ExecutorService executor;

    public UserClient(DiscordUserInfoRepository repo, int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        this.repo = repo;
    }

    private UserResponse fetchUserInfo(String token, String uid) throws IOException {
        val response = repo._user("Bearer " + token, uid).execute();
        val repeat = onError(response, "Cannot exchange user info", UserClient.class);
        if (repeat) return fetchUserInfo(token, uid);

        return response.body();
    }

    private List<GuildsResponse> fetchUserGuilds(String token) throws IOException {
        val response = repo._guilds("Bearer " + token).execute();
        val repeat = onError(response, "Cannot exchange user guilds", UserClient.class);
        if (repeat) return fetchUserGuilds(token);

        return response.body();
    }

    @Override
    public UserResponse getUserInfo(String token, String uid) {
        log.info("getUserInfo: " + token + ", " + uid);
        try {
            return executor.submit(() -> fetchUserInfo(token, uid)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GuildsResponse> getUserGuilds(String token) {
        log.info("getUserGuilds: " + token);

        try {
            return executor.submit(() -> fetchUserGuilds(token)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
