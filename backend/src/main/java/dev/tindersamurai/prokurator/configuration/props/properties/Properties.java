package dev.tindersamurai.prokurator.configuration.props.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component @Slf4j
@PropertySource(value = "classpath:/properties.yml")
public class Properties implements ProkuratorProperties {

	private final Environment environment;

	@Autowired
	public Properties(Environment environment) {
		this.environment = environment;
	}
}