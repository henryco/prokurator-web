package dev.tindersamurai.prokurator.configuration.security.filter.jwt.props;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component @Slf4j
@PropertySource(value = "classpath:/jwt.properties")
public class YmlBasedJwtSecretProperties implements JwtSecretProperties {

	private final Environment environment;

	@Autowired
	public YmlBasedJwtSecretProperties(Environment environment) {
		this.environment = environment;
	}

	@Override
	public String getJwtSecretKey() {
		return environment.getRequiredProperty("secret");
	}
}
