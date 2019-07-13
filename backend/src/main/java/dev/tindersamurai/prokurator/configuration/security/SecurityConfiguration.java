package dev.tindersamurai.prokurator.configuration.security;

import dev.tindersamurai.prokurator.configuration.security.ajax.AjaxAwareAuthEntryPoint;
import dev.tindersamurai.prokurator.configuration.security.auth.AuthenticationProcessor;
import dev.tindersamurai.prokurator.configuration.security.auth.DiscordAuthProcessor;
import dev.tindersamurai.prokurator.configuration.security.filter.JwtAuthenticationFilter;
import dev.tindersamurai.prokurator.configuration.security.filter.JwtAuthorizationFilter;
import dev.tindersamurai.prokurator.configuration.security.props.JwtSecretProperties;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration @Slf4j @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final JwtSecretProperties secretProperties;

	@Autowired
	public SecurityConfiguration(JwtSecretProperties secretProperties) {
		this.secretProperties = secretProperties;
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
				.addFilter(authenticationFilter())
				.addFilter(authorizationFilter())
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		;
	}

	private AbstractAuthenticationProcessingFilter authenticationFilter() throws Exception {
		val authUrl = "/api/auth/login";
		return new JwtAuthenticationFilter(authenticationProcessor(), secretProperties, authUrl);
	}

	private BasicAuthenticationFilter authorizationFilter() throws Exception {
		return new JwtAuthorizationFilter(authenticationManager(), secretProperties);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user")
				.password(passwordEncoder().encode("password"))
				.authorities("ROLE_USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		val source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Bean
	public AuthenticationProcessor authenticationProcessor() throws Exception {
		return new DiscordAuthProcessor(authenticationManager());
	}
}
