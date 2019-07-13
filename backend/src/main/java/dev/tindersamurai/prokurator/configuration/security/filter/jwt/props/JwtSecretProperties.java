package dev.tindersamurai.prokurator.configuration.security.filter.jwt.props;

public interface JwtSecretProperties {

	// JWT token defaults
	String TOKEN_HEADER = "Authorization";
	String TOKEN_PREFIX = "Bearer ";
	String TOKEN_TYPE = "JWT";
	String TOKEN_ISSUER = "secure-api";
	String TOKEN_AUDIENCE = "secure-app";

	String getJwtSecretKey();

	default String getJwtTokenHeader() {
		return TOKEN_HEADER;
	}

	default String getJwtTokenPrefix() {
		return TOKEN_PREFIX;
	}

	default String getJwtTokenType() {
		return TOKEN_TYPE;
	}

	default String getJwtTokenIssuer() {
		return TOKEN_ISSUER;
	}

	default String getJwtTokenAudience() {
		return TOKEN_AUDIENCE;
	}
}
