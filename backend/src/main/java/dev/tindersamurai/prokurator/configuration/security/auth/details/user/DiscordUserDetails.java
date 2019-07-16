package dev.tindersamurai.prokurator.configuration.security.auth.details.user;

import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public interface DiscordUserDetails extends UserDetails {

	@Value class TokenDetails {
		private String access;
		private String refresh;
		private long expires;
	}

	String getDiscordId();

	TokenDetails getToken();

	@Override
	default String getUsername() {
		return getDiscordId();
	}

	@Override
	default Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	default String getPassword() {
		return null;
	}

	@Override
	default boolean isAccountNonExpired() {
		return true;
	}

	@Override
	default boolean isAccountNonLocked() {
		return true;
	}

	@Override
	default boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	default boolean isEnabled() {
		return true;
	}
}
