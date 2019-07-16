package dev.tindersamurai.prokurator.discord.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.tindersamurai.prokurator.discord.client.util.BaseURL;
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

	@Value class TokenResponse {
		private @JsonProperty("access_token") String accessToken;
		private @JsonProperty("refresh_token") String refreshToken;
		private @JsonProperty("token_type") String tokenType;
		private @JsonProperty("expires_in") long expiresIn;
		private @JsonProperty("scope") String scope;
	}

//	@POST("token") @FormUrlEncoded @Headers("Content-Type: application/x-www-form-urlencoded")
//	Call<TokenResponse> _exchange(
//			@Field("code") String code,
//			@Field("grant_type") String type,
//			@Field("client_id") String clientId,
//			@Field("redirect_uri") String redirectUrl,
//			@Field("client_secret") String clientSecret,
//			@Field("scope") String scope
//	);

	@POST("token") @FormUrlEncoded @Headers("Content-Type: application/x-www-form-urlencoded")
	Call<TokenResponse> _exchange(@FieldMap Map<String, String> fields);

	default TokenResponse exchange(TokenExchangeForm data) {
		val map = new HashMap<String, String>(); {
			map.put("code", data.getCode());
			map.put("grant_type", "authorization_code");
			map.put("client_id", data.getClientId());
			map.put("redirect_uri", data.getRedirectUrl());
			map.put("client_secret", data.getClientSecret());
			map.put("scope", data.getScope());
		}

		try {
//			val call = this._exchange(
//					data.getCode(),
//					"authorization_code",
//					data.getClientId(),
//					data.getRedirectUrl(),
//					data.getClientSecret(),
//					data.getScope()
//			);

			val call = this._exchange(map);

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
