package dev.tindersamurai.prokurator.mvc.service.user;

import lombok.Value;

import java.util.List;

public interface UserDataService {

	final class TokenExpiredException extends Exception {
		public TokenExpiredException(String token) {
			super("Token expired: " + token);
		}
	}

	@Value class Token {
		private String access;
		private String refresh;
	}

	@Value class UserData {
		private String id;
		private String username;
		private String discriminator;
		private String avatar;
	}

	@Value class Guild {
		private String id;
		private String name;
		private String icon;
		private String owner;
		private String permissions;
	}

	UserData retrieveUserData(String userAccessToken) throws TokenExpiredException;

	List<Guild> retrieveUserGuilds(String userAccessToken) throws TokenExpiredException;
}
