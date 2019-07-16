package dev.tindersamurai.prokurator.configuration.security.auth.session;

import dev.tindersamurai.prokurator.configuration.security.auth.session.data.DiscordTokenEntity;
import dev.tindersamurai.prokurator.configuration.security.auth.session.data.DiscordTokenRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

@Service @Slf4j
public class DiscordWhitelistService implements WhitelistService {

	private final DiscordTokenRepository tokenRepository;

	@Autowired
	public DiscordWhitelistService(DiscordTokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override @Transactional
	public void addTokenToWhiteList(@NonNull Serializable username, @NonNull String tokenId, String... optionalData) {
		val token = new DiscordTokenEntity(); {
			token.setCreated(Calendar.getInstance().getTime());
			token.setUsername(username.toString());
			token.setId(tokenId);

			if (optionalData != null) {
				val reduce = Arrays.stream(optionalData).reduce((s, s2) -> s + " | " + s2);
				reduce.ifPresent(token::setExtraData);
			}
		}
		tokenRepository.save(token);
	}

	@Override @Transactional
	public boolean isTokenPresent(String tokenId) {
		if (tokenId == null || tokenId.isEmpty())
			return false;
		return tokenRepository.existsById(tokenId);
	}

	@Override  @Transactional
	public void removeToken(@NonNull String tokenId) {
		tokenRepository.deleteById(tokenId);
	}

	@Override @Transactional
	public void removeAllUserTokens(@NonNull Serializable username) {
		tokenRepository.deleteAllByUsername(username.toString());
	}
}
