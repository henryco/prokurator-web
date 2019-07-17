package dev.tindersamurai.prokurator.configuration.security.auth.details;

import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import dev.tindersamurai.prokurator.configuration.security.auth.details.user.DefaultDiscordUserDetails;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository.TokenExchangeForm;
import dev.tindersamurai.prokurator.discord.client.DiscordUserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static dev.tindersamurai.prokurator.configuration.security.auth.details.user.DiscordUserDetails.*;

@Service @Slf4j
public class DiscordDetailsService implements IDiscordDetailsService {

	private final DiscordTokenExchangeRepository exchangeRepository;
	private final DiscordUserInfoRepository userInfoRepository;
	private final ProkuratorSecrets secrets;

	public DiscordDetailsService(
			DiscordTokenExchangeRepository exchangeRepository,
			DiscordUserInfoRepository userInfoRepository,
			ProkuratorSecrets secrets
	) {
		this.exchangeRepository = exchangeRepository;
		this.userInfoRepository = userInfoRepository;
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

		val exchange = exchangeRepository.exchange(exchangeForm);

		val token = new TokenDetails(
				exchange.getAccessToken(),
				exchange.getRefreshToken(),
				now() + exchange.getExpiresIn()
		);

		val user = userInfoRepository.getUserInfo(token.getAccess());

		return new DefaultDiscordUserDetails(user.getId(), token, "USER");
	}

	private static long now() {
		return Calendar.getInstance().getTimeInMillis() - 10000;
	}
}
