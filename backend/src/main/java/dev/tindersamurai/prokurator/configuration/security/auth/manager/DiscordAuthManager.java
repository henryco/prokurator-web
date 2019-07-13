package dev.tindersamurai.prokurator.configuration.security.auth.manager;

import dev.tindersamurai.prokurator.configuration.security.auth.details.DiscordDetailsService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component @Slf4j
public class DiscordAuthManager implements AuthenticationManager {

	private final DiscordDetailsService discordDetailsService;

	@Autowired
	public DiscordAuthManager(DiscordDetailsService discordDetailsService) {
		this.discordDetailsService = discordDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		val discordTokenCode = Objects.toString(authentication.getPrincipal());
		val userDetails = discordDetailsService.loadByDiscordTokenCode(discordTokenCode);

		// TODO DISCORD AUTH FLOW

		return new UsernamePasswordAuthenticationToken(userDetails, null);
	}
}
