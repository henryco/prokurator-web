package dev.tindersamurai.prokurator.configuration.security.details;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ProkuratorAuthToken extends UsernamePasswordAuthenticationToken {

	private @Getter String code;

	public ProkuratorAuthToken(Object principal, Collection<? extends GrantedAuthority> authorities, String code) {
		super(principal, "", authorities);
		this.code = code;
		super.setAuthenticated(true);
	}

	public ProkuratorAuthToken(Object principal, String code) {
		super(principal, "");
		this.code = code;
		super.setAuthenticated(false);
	}
}
