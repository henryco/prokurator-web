package dev.tindersamurai.prokurator.configuration.security.auth.session;

import lombok.Value;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public interface WhitelistService {

	@Value class Token {
		private String tokenId;
		private String access;
		private String refresh;
		private long expired;
	}

	void addTokenToWhiteList(@NonNull Serializable userId, @NonNull Token token, String ... optionalData);

	boolean isTokenPresent(String tokenId);

	void removeToken(@NonNull String tokenId);

	void removeAllUserTokens(@NonNull Serializable userId);

	default void tokenShouldPresent(String tokenId) {
		if (!isTokenPresent(tokenId))
			throw new TokenWhitelistException();
	}
}
