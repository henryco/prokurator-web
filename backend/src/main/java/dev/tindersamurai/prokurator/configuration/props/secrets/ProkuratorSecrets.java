package dev.tindersamurai.prokurator.configuration.props.secrets;

public interface ProkuratorSecrets {
	String getClientId();

	String getClientSecret();

	String getOAuthScope();

	String getOAuthRedirect();

	String getBotToken();
}
