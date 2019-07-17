package dev.tindersamurai.prokurator.configuration.security.auth.session.service.whitelist;

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
import java.util.Date;

@Service @Slf4j
public class DiscordWhitelistService implements TokenWhitelistService {

	private final DiscordTokenRepository tokenRepository;

	@Autowired
	public DiscordWhitelistService(DiscordTokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}


	@Override @Transactional
	public void addTokenToWhiteList(Serializable userId, Token data, String... optionalData) {
		log.debug("addTokenToWhiteList: {}, {}, {}", userId, data, optionalData);
		val token = new DiscordTokenEntity(); {
			token.setCreated(Calendar.getInstance().getTime());
			token.setExpires(new Date(data.getExpires()));
			token.setUserId(userId.toString());
			token.setRefreshToken(data.getRefresh());
			token.setAccessToken(data.getAccess());
			token.setId(data.getTokenId());

			if (optionalData != null) {
				val reduce = Arrays.stream(optionalData).reduce((s, s2) -> s + " | " + s2);
				reduce.ifPresent(token::setExtraData);
			}
		}
		tokenRepository.save(token);
	}

	@Override @Transactional
	public boolean isTokenPresent(String tokenId) {
		log.debug("isTokenPresent: {}", tokenId);
		if (tokenId == null || tokenId.isEmpty())
			return false;
		return tokenRepository.existsById(tokenId);
	}

	@Override  @Transactional
	public void removeToken(@NonNull String tokenId) {
		log.debug("removeToken: {}", tokenId);
		tokenRepository.deleteById(tokenId);
	}

	@Override @Transactional
	public void removeAllUserTokens(@NonNull Serializable userId) {
		log.debug("removeAllUserToken: {}", userId);
		tokenRepository.deleteAllByUserId(userId.toString());
	}
}
