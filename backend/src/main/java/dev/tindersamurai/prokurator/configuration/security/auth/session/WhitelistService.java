package dev.tindersamurai.prokurator.configuration.security.auth.session;

import org.springframework.lang.NonNull;

import java.io.Serializable;

public interface WhitelistService {

	void addTokenToWhiteList(
			@NonNull Serializable username,
			@NonNull String token,
			String... optionalData
	);

	boolean isTokenPresent(String tokenId);

	void removeToken(@NonNull String tokenId);

	void removeAllUserTokens(@NonNull Serializable username);

	default void tokenShouldPresent(String tokenId) {
		if (!isTokenPresent(tokenId))
			throw new TokenWhitelistException();
	}
}
