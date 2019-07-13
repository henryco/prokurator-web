package dev.tindersamurai.prokurator.configuration.security.auth.credentials;

import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class DiscordAuthenticationToken extends AbstractAuthenticationToken {

	private final String code;

	public DiscordAuthenticationToken(@NonNull String discordTokenCode) {
		super(Collections.emptyList());
		this.code = discordTokenCode;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return code;
	}
}
