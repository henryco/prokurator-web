package dev.tindersamurai.prokurator.configuration.security.details;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ProkuratorAuthToken extends UsernamePasswordAuthenticationToken {

	public ProkuratorAuthToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(principal, "", authorities);
		super.setAuthenticated(true);
	}

	public ProkuratorAuthToken(String code) {
		super(code, "");
		super.setAuthenticated(false);
	}

	public String getDiscordTokenCode() {
		return this.getName();
	}
}
