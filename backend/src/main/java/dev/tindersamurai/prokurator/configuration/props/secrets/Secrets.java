package dev.tindersamurai.prokurator.configuration.props.secrets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component @Slf4j
public class Secrets implements ProkuratorSecrets {

	private final Environment environment;

	@Autowired
	public Secrets(Environment environment) {
		this.environment = environment;
	}

	@Override
	public String getClientId() {
		return environment.getRequiredProperty("prokurator.secrets.discord.client.id");
	}

	@Override
	public String getClientSecret() {
		return environment.getRequiredProperty("prokurator.secrets.discord.client.secret");
	}

	@Override
	public String getOAuthScope() {
		return environment.getRequiredProperty("prokurator.secrets.discord.oauth.scope");
	}

	@Override
	public String getOAuthRedirect() {
		return environment.getRequiredProperty("prokurator.secrets.discord.oauth.redirect");
	}

	@Override
	public String getBotToken() {
		return environment.getRequiredProperty("prokurator.secrets.discord.bot.token");
	}
}
