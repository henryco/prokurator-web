package dev.tindersamurai.prokurator.configuration.security.auth.details;

import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenExchangeRepository.TokenExchangeForm;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class DiscordDetailsService implements IDiscordDetailsService {

	private final DiscordTokenExchangeRepository exchangeRepository;
	private final ProkuratorSecrets secrets;

	public DiscordDetailsService(
			DiscordTokenExchangeRepository exchangeRepository,
			ProkuratorSecrets secrets
	) {
		this.exchangeRepository = exchangeRepository;
		this.secrets = secrets;
	}

	@Override
	public UserDetails loadUserByUsername(String discordCode) throws UsernameNotFoundException {

		val exchangeForm = new TokenExchangeForm(
				discordCode,
				"authorization_code",
				secrets.getClientId(),
				"",
				secrets.getClientSecret(),
				"identify guilds"
		);

		val exchange = exchangeRepository.exchange(exchangeForm);
		val token = exchange.getAccessToken();

		return new DiscordUserDetails(token);
	}
}
