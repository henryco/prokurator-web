package dev.tindersamurai.prokurator.configuration.security.auth.processor;

import dev.tindersamurai.prokurator.configuration.security.auth.credentials.DiscordAuthenticationToken;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DiscordAuthProcessor implements AuthenticationProcessor {

	private final AuthenticationManager authenticationManager;

	public DiscordAuthProcessor(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		val code = request.getParameter("discord_code");
		return authenticationManager.authenticate(new DiscordAuthenticationToken(code));
	}
}
