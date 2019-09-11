package dev.tindersamurai.prokurator.configuration.security.auth.session.service.access;

import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import dev.tindersamurai.prokurator.configuration.security.auth.session.data.DiscordTokenRepository;
import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.TokenRefreshForm;
import dev.tindersamurai.prokurator.discord.client.DiscordTokenClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service @Slf4j
public class DiscordTokenService implements TokenAccessService {

	private final DiscordTokenRepository tokenRepository;
	private final DiscordTokenClient discordTokenClient;
	private final ProkuratorSecrets secrets;

	@Autowired
	public DiscordTokenService(
			DiscordTokenRepository tokenRepository,
			DiscordTokenClient discordTokenClient,
			ProkuratorSecrets secrets
	) {

		this.tokenRepository = tokenRepository;
		this.discordTokenClient = discordTokenClient;
		this.secrets = secrets;
	}

	@Override @Transactional
	public Token getToken(String tokenId) {
		log.debug("getToken: {}", tokenId);
		val one = tokenRepository.getOne(tokenId);
		log.debug("FOUND: {}", one);
		if (one.getExpires().before(now())) {
			val refreshForm = new TokenRefreshForm(one.getRefreshToken(),
					secrets.getClientSecret(), secrets.getOAuthRedirect(),
					secrets.getClientId(), secrets.getOAuthScope()
			);

			log.debug("TOKEN EXPIRED: {}", refreshForm);

			val response = discordTokenClient.refresh(refreshForm);
			one.setExpires(expTime(response.getExpiresIn()));
			one.setRefreshToken(response.getRefreshToken());
			one.setAccessToken(response.getAccessToken());
			tokenRepository.save(one);

			log.debug("REFRESHED: {}", one);
		}

		return new Token(one.getId(), one.getAccessToken(),
				one.getRefreshToken(), one.getExpires()
		);
	}

	private static Date expTime(long expires) {
		return new Date(Calendar.getInstance().getTimeInMillis() - 10000 + expires);
	}

	private static Date now() {
		return Calendar.getInstance().getTime();
	}
}
