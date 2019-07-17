package dev.tindersamurai.prokurator.configuration.security.auth.credentials;

import lombok.Value;

@Value public class DiscordTokenPrincipal {
	private String discordId;
	private String tokenId;
}
