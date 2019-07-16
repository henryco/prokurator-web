package dev.tindersamurai.prokurator.configuration.security.auth.details.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

@ToString
public class DefaultDiscordUserDetails implements DiscordUserDetails {

	private final TokenDetails tokenDetails;
	private final String[] authorities;
	private final String discordId;

	public DefaultDiscordUserDetails(String discordId, TokenDetails tokenDetails, String ... authorities) {
		this.tokenDetails = tokenDetails;
		this.authorities = authorities;
		this.discordId = discordId;
	}

	@Override
	public String getDiscordId() {
		return discordId;
	}

	@Override
	public TokenDetails getToken() {
		return tokenDetails;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(authorities);
	}
}
