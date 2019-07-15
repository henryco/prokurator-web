package dev.tindersamurai.prokurator.discord.client;

import com.google.gson.annotations.SerializedName;
import dev.tindersamurai.prokurator.discord.client.util.BaseURL;
import lombok.Value;
import lombok.val;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.HashMap;
import java.util.Map;

@BaseURL("https://discordapp.com/api/oauth2/token/")
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
		private @SerializedName("access_token") String accessToken;
		private @SerializedName("refresh_token") String refreshToken;
		private @SerializedName("token_type") String tokenType;
		private @SerializedName("expires_in") long expiresIn;
		private String scope;
	}

	@POST("/") @FormUrlEncoded @Headers("Content-Type: application/x-www-form-urlencoded")
	Call<TokenResponse> _exchange(@FieldMap Map<String, String> fields);

	default TokenResponse exchange(TokenExchangeForm data) {
		val map = new HashMap<String, String>(); {
			map.put("code", data.getCode());
			map.put("grant_type", data.getGrantType());
			map.put("client_id", data.getClientId());
			map.put("redirect_url", data.getRedirectUrl());
			map.put("client_secret", data.getClientSecret());
			map.put("scope", data.getScope());
		}
		try {
			val response = this._exchange(map).execute();
			return response.body();
		} catch (Exception e) {
			throw new RuntimeException("Cannot exchange discord token", e);
		}
	}
}
