package dev.tindersamurai.prokurator.configuration.security.auth.details;

import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import dev.tindersamurai.prokurator.configuration.security.auth.details.user.DefaultDiscordUserDetails;
import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.TokenExchangeForm;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenClient;
import dev.tindersamurai.prokurator.discord.client.DiscordUserClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

import static dev.tindersamurai.prokurator.configuration.security.auth.details.user.DiscordUserDetails.*;

@Service @Slf4j
public class DiscordDetailsService implements IDiscordDetailsService {

	private final DiscordTokenClient discordTokenClient;
	private final DiscordUserClient discordUserClient;
	private final ProkuratorSecrets secrets;

	public DiscordDetailsService(
			DiscordTokenClient discordTokenClient,
			DiscordUserClient discordUserClient,
			ProkuratorSecrets secrets
	) {

		this.discordTokenClient = discordTokenClient;
		this.discordUserClient = discordUserClient;
		this.secrets = secrets;
	}

	@Override
	public UserDetails loadUserByUsername(String discordCode) throws UsernameNotFoundException {

		val exchangeForm = new TokenExchangeForm(
				discordCode,
				secrets.getClientId(),
				secrets.getOAuthRedirect(),
				secrets.getClientSecret(),
				secrets.getOAuthScope()
		);

		val exchange = discordTokenClient.exchange(exchangeForm);

		val token = new TokenDetails(
				UUID.randomUUID().toString(),
				exchange.getAccessToken(),
				exchange.getRefreshToken(),
				now() + exchange.getExpiresIn()
		);

		val user = discordUserClient.getUserInfo(token.getAccess());

		return new DefaultDiscordUserDetails(user.getId(), token, "USER");
	}

	private static long now() {
		return Calendar.getInstance().getTimeInMillis() - 10000;
	}
}
