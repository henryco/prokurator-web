package dev.tindersamurai.prokurator.configuration.security.details.service;

import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface IProkuratorDetailsService {

	ProkuratorUserDetails loadByDiscordTokenCode(@NonNull String discordTokenCode) throws UsernameNotFoundException;

	@ToString
	class ProkuratorUserDetails implements UserDetails {

		private final String discordId;

		/* package */ ProkuratorUserDetails(String discordId) {
			this.discordId = discordId;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return AuthorityUtils.createAuthorityList("USER");
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public String getUsername() {
			return discordId;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}
