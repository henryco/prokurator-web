package dev.tindersamurai.prokurator.configuration.security.auth.session;

public class TokenWhitelistException extends RuntimeException {

	public TokenWhitelistException(String message) {
		super(message);
	}

	public TokenWhitelistException() {
		this("Token not present on whitelist");
	}
}
