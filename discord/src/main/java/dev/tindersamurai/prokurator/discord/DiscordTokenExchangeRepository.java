package dev.tindersamurai.prokurator.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.tindersamurai.prokurator.discord.util.BaseURL;
import lombok.Value;
import lombok.val;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.Map;

@BaseURL("https://discordapp.com/api/oauth2/")
public interface DiscordTokenExchangeRepository {

	@Value class TokenExchangeForm {
		private String code;
		private String clientId;
		private String redirectUrl;
		private String clientSecret;
		private String scope;
	}

	@Value class TokenRefreshForm {
		private String refreshToken;
		private String clientSecret;
		private String redirectUrl;
		private String clientId;
		private String scope;
	}

	@Value class TokenResponse {
		private @JsonProperty("access_token") String accessToken;
		private @JsonProperty("refresh_token") String refreshToken;
		private @JsonProperty("token_type") String tokenType;
		private @JsonProperty("expires_in") long expiresIn;
		private @JsonProperty("scope") String scope;
	}

	@POST("token") @FormUrlEncoded @Headers("Content-Type: application/x-www-form-urlencoded")
	Call<TokenResponse> _exchange(@FieldMap Map<String, String> fields);

}
