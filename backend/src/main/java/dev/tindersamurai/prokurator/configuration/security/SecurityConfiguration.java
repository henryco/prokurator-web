package dev.tindersamurai.prokurator.configuration.security;

import dev.tindersamurai.prokurator.configuration.security.ajax.AjaxAwareAuthEntryPoint;
import dev.tindersamurai.prokurator.configuration.security.auth.processor.AuthenticationProcessor;
import dev.tindersamurai.prokurator.configuration.security.auth.processor.DiscordAuthProcessor;
import dev.tindersamurai.prokurator.configuration.security.auth.session.WhitelistService;
import dev.tindersamurai.prokurator.configuration.security.filter.JwtAuthenticationFilter;
import dev.tindersamurai.prokurator.configuration.security.filter.JwtAuthorizationFilter;

import dev.tindersamurai.prokurator.configuration.security.filter.JwtLogoutFilter;
import dev.tindersamurai.prokurator.configuration.security.filter.props.JwtSecretProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration @Slf4j @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AuthenticationManager authenticationManager;
	private final JwtSecretProperties secretProperties;
	private final WhitelistService whitelistService;

	@Autowired
	public SecurityConfiguration(
			AuthenticationManager authenticationManager,
			JwtSecretProperties secretProperties,
			WhitelistService whitelistService
	) {
		this.authenticationManager = authenticationManager;
		this.secretProperties = secretProperties;
		this.whitelistService = whitelistService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
				.csrf().disable()
				.exceptionHandling()
				.authenticationEntryPoint(new AjaxAwareAuthEntryPoint("/#/auth"))
				.and()
				.authorizeRequests()
				.antMatchers("/api/protected/**").authenticated()
				.antMatchers("/api/open/**").permitAll()
				.antMatchers("/resources/**").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/").permitAll()
				.and()
				.addFilter(authenticationFilter("/api/auth/login"))
				.addFilter(authorizationFilter())
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		val source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Bean
	public AuthenticationProcessor authenticationProcessor() {
		return new DiscordAuthProcessor(authenticationManager);
	}

	@Bean
	public FilterRegistrationBean<JwtLogoutFilter> logoutFilterRegistrationBean() {
		val registrationBean = new FilterRegistrationBean<JwtLogoutFilter>(); {
			registrationBean.setFilter(logoutFilter("/api/auth/logout"));
			registrationBean.setOrder(Integer.MAX_VALUE);
		}
		return registrationBean;
	}

	private BasicAuthenticationFilter authorizationFilter() {
		return new JwtAuthorizationFilter(authenticationManager, secretProperties, whitelistService);
	}

	@SuppressWarnings("SameParameterValue")
	private AbstractAuthenticationProcessingFilter authenticationFilter(String url) {
		return new JwtAuthenticationFilter(authenticationProcessor(), secretProperties, whitelistService, url);
	}

	@SuppressWarnings("SameParameterValue")
	private JwtLogoutFilter logoutFilter(String url) {
		return new JwtLogoutFilter(secretProperties, whitelistService, url);
	}
}
