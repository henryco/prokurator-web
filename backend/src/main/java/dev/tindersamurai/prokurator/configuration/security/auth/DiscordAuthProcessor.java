package dev.tindersamurai.prokurator.configuration.security.auth;

import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
		val authenticationToken = new UsernamePasswordAuthenticationToken("user", "password");
		return authenticationManager.authenticate(authenticationToken);
	}
}
