package dev.tindersamurai.prokurator.configuration.security.auth.details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IDiscordDetailsService extends UserDetailsService {

	default UserDetails loadByDiscordTokenCode(String code) throws UsernameNotFoundException {
		return loadUserByUsername(code);
	}
}
