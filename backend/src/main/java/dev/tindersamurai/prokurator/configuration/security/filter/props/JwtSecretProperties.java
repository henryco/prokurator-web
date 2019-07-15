package dev.tindersamurai.prokurator.configuration.security.filter.props;

public interface JwtSecretProperties {

	String getJwtSecretKey();

	default String getJwtTokenHeader() {
		return "Authorization";
	}

	default String getJwtTokenPrefix() {
		return "Bearer ";
	}

	default String getJwtTokenType() {
		return "JWT";
	}

	default String getJwtTokenIssuer() {
		return "secure-api";
	}

	default String getJwtTokenAudience() {
		return "secure-app";
	}

	default long getJwtTokenLiveTime() {
		return 864000000L;
	}
}
