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

	default TokenResponse _exchange_(Map<String, String> map) {
		try {
			val response = this._exchange(map).execute();
			if (!response.isSuccessful())
				throw new RuntimeException("Cannot exchange discord token: "
						+ (response.errorBody() == null ? "" : response.errorBody().string())
				);
			return response.body();
		} catch (Exception e) {
			throw new RuntimeException("Cannot exchange discord token", e);
		}
	}

	default TokenResponse exchange(TokenExchangeForm data) {
		val map = new HashMap<String, String>(); {
			map.put("code", data.getCode());
			map.put("grant_type", "authorization_code");
			map.put("client_id", data.getClientId());
			map.put("redirect_uri", data.getRedirectUrl());
			map.put("client_secret", data.getClientSecret());
			map.put("scope", data.getScope());
		}
		return _exchange_(map);
	}

	default TokenResponse refresh(TokenRefreshForm data) {
		val map = new HashMap<String, String>(); {
			map.put("client_id", data.getClientId());
			map.put("client_secret", data.getClientSecret());
			map.put("grant_type", "refresh_token");
			map.put("refresh_token", data.getRefreshToken());
			map.put("redirect_uri", data.getRedirectUrl());
			map.put("scope", data.getScope());
		}
		return _exchange_(map);
	}
}
