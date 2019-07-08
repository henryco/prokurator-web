package dev.tindersamurai.prokurator.configuration.security;

import dev.tindersamurai.prokurator.configuration.security.ajax.AjaxAwareAuthEntryPoint;
import dev.tindersamurai.prokurator.configuration.security.details.auth.ProkuratorAuthFilter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration @Slf4j @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AbstractUserDetailsAuthenticationProvider authenticationProvider;
	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;

	@Autowired
	public ApiSecurityConfiguration(
			AbstractUserDetailsAuthenticationProvider authenticationProvider,
			AuthenticationSuccessHandler successHandler,
			AuthenticationFailureHandler failureHandler
	) {
		this.authenticationProvider = authenticationProvider;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
				.authenticationEntryPoint(new AjaxAwareAuthEntryPoint("/#/login"))
				.and()
				.authorizeRequests()
				.anyRequest().authenticated()
				.antMatchers("/api/protected/**").hasRole("USER")
				.antMatchers("/api/open/**").permitAll()

				.antMatchers("/resources/**").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/static/**").permitAll()

				.antMatchers("/login/**").permitAll()
				.antMatchers("/").permitAll()

				.and()
				.formLogin()
				.loginPage("/login")

				.and()
				.logout()
		;
	}

	private Filter authenticationFilter() throws Exception {
		val filter = new ProkuratorAuthFilter(); {
			filter.setAuthenticationManager(authenticationManagerBean());
			filter.setAuthenticationFailureHandler(failureHandler);
			filter.setAuthenticationSuccessHandler(successHandler);
		}
		return filter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}



}
