package dev.tindersamurai.prokurator.discord.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.tindersamurai.prokurator.discord.client.util.BaseURL;
import lombok.Value;
import lombok.val;
import retrofit2.Call;
import retrofit2.http.*;

@BaseURL("https://discordapp.com/api/oauth2/")
public interface DiscordTokenExchangeRepository {

	@Value class TokenExchangeForm {
		private String code;
		private String grantType;
		private String clientId;
		private String redirectUrl;
		private String clientSecret;
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
	Call<TokenResponse> _exchange(
			@Field("code") String code,
			@Field("grant_type") String type,
			@Field("client_id") String clientId,
			@Field("redirect_uri") String redirectUrl,
			@Field("client_secret") String clientSecret,
			@Field("scope") String scope
	);

	default TokenResponse exchange(TokenExchangeForm data) {
		try {
			val call = this._exchange(
					data.getCode(),
					data.getGrantType(),
					data.getClientId(),
					data.getRedirectUrl(),
					data.getClientSecret(),
					data.getScope()
			);

			val response = call.execute();

			if (!response.isSuccessful())
				throw new RuntimeException("Cannot exchange discord token: "
						+ (response.errorBody() == null ? "" : response.errorBody().string())
				);

			return response.body();
		} catch (Exception e) {
			throw new RuntimeException("Cannot exchange discord token", e);
		}
	}
}
