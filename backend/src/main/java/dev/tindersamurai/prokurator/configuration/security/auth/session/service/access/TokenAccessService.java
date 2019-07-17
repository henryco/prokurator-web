package dev.tindersamurai.prokurator.configuration.security.auth.session.service.access;

import lombok.Value;

import java.util.Date;

public interface TokenAccessService {

	@Value class Token {
		private String tokenId;
		private String access;
		private String refresh;
		private Date expires;
	}

	Token getToken(String tokenId);

}
