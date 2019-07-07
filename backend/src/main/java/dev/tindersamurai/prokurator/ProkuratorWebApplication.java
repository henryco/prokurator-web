package dev.tindersamurai.prokurator;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication @Slf4j
public class ProkuratorWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProkuratorWebApplication.class, args);
	}

	@Bean @Profile("dev")
	public Server h2Server() {
		val server = new Server();
		try {
			server.runTool("-tcp");
			server.runTool("-tcpAllowOthers");
		} catch (Exception e) {
			log.error("Cannot setup h2 server", e);
		}
		return server;
	}

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		val loggingFilter = new CommonsRequestLoggingFilter(); {
			loggingFilter.setIncludeClientInfo(true);
			loggingFilter.setIncludeQueryString(true);
			loggingFilter.setIncludePayload(true);
			loggingFilter.setIncludeHeaders(true);
		}
		return loggingFilter;
	}
}
