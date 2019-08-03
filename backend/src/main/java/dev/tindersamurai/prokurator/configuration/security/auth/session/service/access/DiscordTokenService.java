package dev.tindersamurai.prokurator.configuration.security.auth.session.service.access;

import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import dev.tindersamurai.prokurator.configuration.security.auth.session.data.DiscordTokenRepository;
import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository;
import dev.tindersamurai.prokurator.discord.DiscordTokenExchangeRepository.TokenRefreshForm;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service @Slf4j
public class DiscordTokenService implements TokenAccessService {

	private final DiscordTokenExchangeRepository exchangeRepository;
	private final DiscordTokenRepository tokenRepository;
	private final ProkuratorSecrets secrets;

	@Autowired
	public DiscordTokenService(
			DiscordTokenExchangeRepository exchangeRepository,
			DiscordTokenRepository tokenRepository,
			ProkuratorSecrets secrets
	) {
		this.exchangeRepository = exchangeRepository;
		this.tokenRepository = tokenRepository;
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

			val response = exchangeRepository.refresh(refreshForm);
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
