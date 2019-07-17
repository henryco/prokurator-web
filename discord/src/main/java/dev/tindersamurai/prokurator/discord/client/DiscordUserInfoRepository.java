package dev.tindersamurai.prokurator.discord.client;

import dev.tindersamurai.prokurator.discord.client.util.BaseURL;
import lombok.Value;
import lombok.val;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

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

	@GET("@me")
	Call<UserResponse> _user(@Header("Authorization") String authorization);

	@GET("@me/guilds")
	Call<List<GuildsResponse>> _guilds(@Header("Authorization") String authorization);


	default UserResponse getUserInfo(String token) {
		try {
			val response = _user("Bearer " + token).execute();
			if (!response.isSuccessful())
				throw new RuntimeException("Cannot exchange user info: "
						+ (response.errorBody() == null ? "" : response.errorBody().string())
				);
			return response.body();
		} catch (Exception e) {
			throw new RuntimeException("Cannot exchange user info", e);
		}
	}

	default List<GuildsResponse> getUserGuilds(String token) {
		try {
			val response = _guilds("Bearer " + token).execute();
			if (!response.isSuccessful())
				throw new RuntimeException("Cannot exchange user guilds: "
						+ (response.errorBody() == null ? "" : response.errorBody().string())
				);
			return response.body();
		} catch (Exception e) {
			throw new RuntimeException("Cannot exchange user guilds", e);
		}
	}
}
