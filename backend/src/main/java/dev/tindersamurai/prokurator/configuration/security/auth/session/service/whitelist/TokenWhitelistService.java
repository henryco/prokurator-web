package dev.tindersamurai.prokurator.configuration.security.auth.session.service.whitelist;

import dev.tindersamurai.prokurator.configuration.security.auth.session.exception.TokenWhitelistException;
import lombok.Value;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public interface TokenWhitelistService {

	@Value class Token {
		private String tokenId;
		private String access;
		private String refresh;
		private long expires;
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
