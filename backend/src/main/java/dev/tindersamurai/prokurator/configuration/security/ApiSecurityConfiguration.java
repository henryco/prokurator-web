package dev.tindersamurai.prokurator.configuration.security;

import dev.tindersamurai.prokurator.configuration.security.ajax.AjaxAwareAuthEntryPoint;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration @Slf4j @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public ApiSecurityConfiguration(
			@Qualifier("prokuratorDetailsService") UserDetailsService userDetailsService,
			AuthenticationSuccessHandler successHandler,
			AuthenticationFailureHandler failureHandler,
			PasswordEncoder passwordEncoder
	) {
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;

		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.exceptionHandling()
				.authenticationEntryPoint(new AjaxAwareAuthEntryPoint("/#/login"))
				.and()
				.authorizeRequests()
				.anyRequest().permitAll()
				.antMatchers("/api/protected/**").hasRole("USER")
				.antMatchers("/api/open/**").permitAll()
				.and()
				.formLogin()
				.successHandler(successHandler)
				.failureHandler(failureHandler)
				.loginPage("/login")

				.and()
				.logout()
		;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// TODO CUSTOM AUTHENTICATION PROVIDER
		val provider = new DaoAuthenticationProvider(); {
			provider.setUserDetailsService(userDetailsService);
			provider.setPasswordEncoder(passwordEncoder);
		}

		auth.authenticationProvider(provider)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder)
		;
	}



}
