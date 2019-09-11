package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.DiscordGuildRepository;
import lombok.val;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static dev.tindersamurai.prokurator.discord.DiscordGuildRepository.*;

public class GuildClient implements DiscordGuildClient {

    private final DiscordGuildRepository repo;

    public GuildClient(DiscordGuildRepository repo) {
        this.repo = repo;
    }

    private static void onError(Response r, String msg) throws IOException {
        if (!r.isSuccessful())
            throw new IOException(msg + ": " +
                    (r.errorBody() == null ? "" : r.errorBody().string())
            );
    }

    @Override
    public List<GuildChannel> getGuildChannels(String botToken, String guildId) {
        try {
            val response = repo._guildChannels("Bot " + botToken, guildId).execute();
            onError(response, "Cannot fetch guild channels");
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Cannot fetch guild channels", e);
        }
    }

    @Override
    public List<GuildMember> getGuildMembers(String botToken, String guildId) {
        try {
            val response = repo._guildMembers("Bot " + botToken, guildId, 1000).execute();
            onError(response, "Cannot fetch guild members");
            val list = response.body();
            if (list == null)
                throw new NullPointerException("Users list == null");

            if (list.size() < 1000)
                return list;

            boolean full = false;
            while (!full) {
                val last = list.get(list.size() - 1).getUser().getId();
                val r = repo._guildMembers("Bot " + botToken, guildId,1000, last).execute();

                onError(r, "Cannot fetch guild members");

                val l = r.body();
                if (l == null)
                    throw new NullPointerException("Users list == null");

                list.addAll(l);

                if (l.size() < 1000)
                    full = true;
            }

            return list;
        } catch (IOException e) {
            throw new RuntimeException("Cannot fetch guild members", e);
        }
    }

}
