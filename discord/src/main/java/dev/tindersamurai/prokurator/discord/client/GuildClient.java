package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;
import lombok.val;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.tindersamurai.prokurator.discord.DiscordGuildRepository.*;

public class GuildClient implements DiscordGuildClient {

    private final DiscordGuildRepository repo;
    private final ExecutorService executor;

    public GuildClient(DiscordGuildRepository repo, int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        this.repo = repo;
    }

    private static void onError(Response r, String msg) throws IOException {
        if (!r.isSuccessful()) {
            val err = (r.errorBody() == null ? "" : r.errorBody().string());

            System.out.println("body: " + r.body());
            System.out.println("error: " + err);

            throw new IOException(msg + ": " + err);
        }
    }

    @Override
    public List<GuildChannel> getGuildChannels(String botToken, String guildId) {
        try {
            return executor.submit(() -> fetchGuildChannels(botToken, guildId)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GuildMember> getGuildMembers(String botToken, String guildId) {
        try {
            return executor.submit(() -> fetchGuildMembers(botToken, guildId)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<GuildChannel> fetchGuildChannels(String botToken, String guildId) throws IOException {
        val response = repo._guildChannels("Bot " + botToken, guildId).execute();
        onError(response, "Cannot fetch guild channels");
        return response.body();
    }

    private List<GuildMember> fetchGuildMembers(String botToken, String guildId) throws IOException {

        val response = repo._guildMembers("Bot " + botToken, guildId, 1000).execute();
        onError(response, "Cannot fetch guild members");
        val list = response.body();
        if (list == null)
            throw new IOException("Users list == null");

        if (list.size() < 1000)
            return list;

        boolean full = false;
        while (!full) {
            val last = list.get(list.size() - 1).getUser().getId();
            val r = repo._guildMembers("Bot " + botToken, guildId,1000, last).execute();

            onError(r, "Cannot fetch guild members");

            val l = r.body();
            if (l == null)
                throw new IOException("Users list == null");

            list.addAll(l);

            if (l.size() < 1000)
                full = true;
        }

        return list;
    }

}
