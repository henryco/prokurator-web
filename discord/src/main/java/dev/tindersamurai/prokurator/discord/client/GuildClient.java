package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;
import lombok.extern.java.Log;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.tindersamurai.prokurator.discord.DiscordGuildRepository.*;
import static dev.tindersamurai.prokurator.discord.util.Helper.onError;

@Log
public class GuildClient implements DiscordGuildClient {

    private final DiscordGuildRepository repo;
    private final ExecutorService executor;

    public GuildClient(DiscordGuildRepository repo, int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        this.repo = repo;
    }

    @Override
    public List<GuildChannel> getGuildChannels(String botToken, String guildId) {
        log.info("getGuildChannels: " + botToken + ", " + guildId);
        try {
            return executor.submit(() -> fetchGuildChannels(botToken, guildId)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GuildMember> getGuildMembers(String botToken, String guildId) {
        log.info("getGuildMembers: " + botToken + ", " + guildId);
        try {
            return executor.submit(() -> fetchGuildMembers(botToken, guildId)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<GuildChannel> fetchGuildChannels(String botToken, String guildId) throws IOException {
        val response = repo._guildChannels("Bot " + botToken, guildId).execute();
        val repeat = onError(response, "Cannot fetch guild channels", GuildClient.class);
        if (repeat) return fetchGuildChannels(botToken, guildId);

        return response.body();
    }

    private List<GuildMember> fetchGuildMembers(String botToken, String guildId, String last) throws IOException {
        val r = repo._guildMembers("Bot " + botToken, guildId,1000, last).execute();
        val repeat = onError(r, "Cannot fetch guild members", GuildClient.class);
        if (repeat) return fetchGuildMembers(botToken, guildId, last);

        return r.body();
    }

    private List<GuildMember> fetchGuildMembers(String botToken, String guildId) throws IOException {

        val response = repo._guildMembers("Bot " + botToken, guildId, 1000).execute();
        val repeat = onError(response, "Cannot fetch guild members", GuildClient.class);
        if (repeat) return fetchGuildMembers(botToken, guildId);

        val list = response.body();
        if (list == null)
            throw new IOException("Users list == null");
        if (list.size() < 1000)
            return list;

        boolean full = false;
        while (!full) {
            val last = list.get(list.size() - 1).getUser().getId();
            val l = fetchGuildMembers(botToken, guildId, last);
            if (l == null)
                throw new IOException("Users list == null");
            list.addAll(l);
            if (l.size() < 1000)
                full = true;
        }

        return list;
    }

}
