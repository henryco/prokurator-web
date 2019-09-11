package dev.tindersamurai.prokurator.discord.client;


import java.util.List;

import static dev.tindersamurai.prokurator.discord.DiscordUserInfoRepository.*;

public interface DiscordUserClient {

    UserResponse getUserInfo(String token, String uid);

    List<GuildsResponse> getUserGuilds(String token);

    default UserResponse getUserInfo(String token) {
        return getUserInfo(token, "@me");
    }
}
