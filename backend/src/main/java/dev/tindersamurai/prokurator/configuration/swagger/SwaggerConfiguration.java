package dev.tindersamurai.prokurator.configuration.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 @Order
@Profile({"dev", "develop"})
public class SwaggerConfiguration
		extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				"/v2/api-docs/**",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/swagger/**",
				"/webjars/**"
		);
	}

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.any())
				.build();
	}
}
