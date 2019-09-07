package dev.tindersamurai.prokurator.discord;

import dev.tindersamurai.prokurator.discord.util.BaseURL;
import lombok.Value;
import lombok.val;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

@BaseURL("https://discordapp.com/api/guilds/")
public interface DiscordGuildRepository {

    @Value class UserResponse {
        private String id;
        private String username;
        private String discriminator;
        private String avatar;
        private Boolean bot;
        private Boolean mfa_enabled;
        private String locale;
        private Boolean verified;
        private String email;
        private Integer flags;
        private Integer premium_type;
    }

    @Value class GuildMember {
        private UserResponse user;
        private Boolean mute;
        private Boolean deaf;
    }

    @GET("{gid}/members")
    Call<List<GuildMember>> _guildMembers(
            @Header("Authorization") String authorization,
            @Path("gid") String gid,
            @Query("limit") int limit,
            @Query("after") String afterId
    );

    @GET("{gid}/members")
    Call<List<GuildMember>> _guildMembers(
            @Header("Authorization") String authorization,
            @Path("gid") String gid,
            @Query("limit") int limit
    );

    default List<GuildMember> getGuildMembers(String botToken, String guildId) {
        try {
            val response = _guildMembers("Bot " + botToken, guildId, 1000).execute();
            if (!response.isSuccessful())
                throw new RuntimeException("Cannot exchange guild members: "
                        + (response.errorBody() == null ? "" : response.errorBody().string())
                );
            val list = response.body();
            if (list == null)
                throw new NullPointerException("Users list == null");

            if (list.size() < 1000)
                return list;

            boolean full = false;
            while (!full) {
                val last = list.get(list.size() - 1).getUser().getId();
                val r = _guildMembers("Bot " + botToken, guildId,1000, last).execute();
                if (!r.isSuccessful())
                    throw new RuntimeException("Cannot exchange guild members: "
                            + (r.errorBody() == null ? "" : r.errorBody().string())
                    );

                val l = r.body();
                if (l == null)
                    throw new NullPointerException("Users list == null");

                list.addAll(l);

                if (l.size() < 1000)
                    full = true;
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException("Cannot exchange guild members", e);
        }
    }

}
