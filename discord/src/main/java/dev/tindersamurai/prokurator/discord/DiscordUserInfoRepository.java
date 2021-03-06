package dev.tindersamurai.prokurator.discord;

import dev.tindersamurai.prokurator.discord.util.BaseURL;
import lombok.Value;
import lombok.val;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import java.util.List;

@BaseURL("https://discordapp.com/api/users/")
public interface DiscordUserInfoRepository {

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

	@Value class GuildsResponse {
		private String id;
		private String name;
		private String icon;
		private String owner;
		private String permissions;
	}

	@GET("{uid}")
	Call<UserResponse> _user(@Header("Authorization") String authorization, @Path("uid") String uid);

	@GET("@me/guilds")
	Call<List<GuildsResponse>> _guilds(@Header("Authorization") String authorization);

}
