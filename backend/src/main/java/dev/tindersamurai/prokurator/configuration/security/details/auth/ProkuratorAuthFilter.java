package dev.tindersamurai.prokurator.configuration.security.details.auth;

import dev.tindersamurai.prokurator.configuration.security.details.ProkuratorAuthToken;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ProkuratorAuthFilter extends UsernamePasswordAuthenticationFilter {

	private static final String FORM_DISCORD_TOKEN_CODE_KEY = "discord_code";
	private static final String FORM_DISCORD_TOKEN_ID_KEY = "discord_uid";

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request,
			HttpServletResponse response
	) throws AuthenticationException {
		log.info("attemptAuthentication: {}, {}", request, response);
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod()
			);
		}
		val authRequest = getAuthRequest(request);
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	private ProkuratorAuthToken getAuthRequest(HttpServletRequest request) {
		val id = obtainDiscordId(request);
		val code = obtainTokenCode(request);
		return new ProkuratorAuthToken(id, code);
	}

	private String obtainDiscordId(HttpServletRequest request) {
		val id = request.getParameter(FORM_DISCORD_TOKEN_ID_KEY);
		return id == null ? "" : id;
	}

	private String obtainTokenCode(HttpServletRequest request) {
		val code = request.getParameter(FORM_DISCORD_TOKEN_CODE_KEY);
		return code == null ? "" : code;
	}
}
