package dev.tindersamurai.prokurator.configuration.security.auth.details.user;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

@ToString
public class DefaultDiscordUserDetails implements DiscordUserDetails {

	private @Getter final TokenDetails tokenDetails;
	private @Getter final String discordId;
	private final String[] authorities;

	public DefaultDiscordUserDetails(String discordId, String tokenId, String ... authorities) {
		this.tokenDetails = new TokenDetails(tokenId, null, null, null);
		this.authorities = authorities;
		this.discordId = discordId;
	}

	public DefaultDiscordUserDetails(String discordId, TokenDetails tokenDetails, String ... authorities) {
		this.tokenDetails = tokenDetails;
		this.authorities = authorities;
		this.discordId = discordId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(authorities);
	}
}
