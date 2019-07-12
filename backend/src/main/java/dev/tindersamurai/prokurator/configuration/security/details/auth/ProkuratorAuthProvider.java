package dev.tindersamurai.prokurator.configuration.security.details.auth;

import dev.tindersamurai.prokurator.configuration.security.details.ProkuratorAuthToken;
import dev.tindersamurai.prokurator.configuration.security.details.service.IProkuratorDetailsService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component @Slf4j
public class ProkuratorAuthProvider extends AbstractUserDetailsAuthenticationProvider {

	private final IProkuratorDetailsService detailsService;

	@Autowired
	public ProkuratorAuthProvider(IProkuratorDetailsService detailsService) {
		this.detailsService = detailsService;
	}

	@Override
	protected void additionalAuthenticationChecks(
			UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication
	) throws AuthenticationException {
		log.info("+ additionalAuthenticationChecks + {}, {}", userDetails, authentication);
		val auth = ((ProkuratorAuthToken) authentication);
		log.debug("auth: {}", auth);
	}

	@Override
	protected UserDetails retrieveUser(
			String username,
			UsernamePasswordAuthenticationToken authentication
	) throws AuthenticationException {
		log.info("retrieveUser; {}, {}", username, authentication);
		val auth = ((ProkuratorAuthToken) authentication);
		val details = detailsService.loadByDiscordTokenCode(auth.getDiscordTokenCode());
		log.info("details: {}", details);
		return details;
	}
}
