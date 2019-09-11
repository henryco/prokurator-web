package dev.tindersamurai.prokurator.discord;

import dev.tindersamurai.prokurator.discord.util.BaseURL;
import lombok.Value;
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

    enum Type {
        member, role
    }

    @Value class PermOverwrite {
        private String id;
        private Type type;
        private int allow;
        private int deny;
    }

    @Value class GuildChannel {
        private PermOverwrite[] permission_overwrites;
        private String name;
        private String id;
        private int type;
    }

    @GET("{gid}/members")
    Call<List<GuildMember>> _guildMembers(
            @Header("Authorization") String botToken,
            @Path("gid") String gid,
            @Query("limit") int limit,
            @Query("after") String afterId
    );

    @GET("{gid}/members")
    Call<List<GuildMember>> _guildMembers(
            @Header("Authorization") String botToken,
            @Path("gid") String gid,
            @Query("limit") int limit
    );

    @GET("{gid}/channels")
    Call<List<GuildChannel>> _guildChannels(
            @Header("Authorization") String botToken,
            @Path("gid") String gid
    );

}
