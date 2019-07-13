package dev.tindersamurai.prokurator.configuration.security.filter;

import dev.tindersamurai.prokurator.configuration.security.auth.AuthenticationProcessor;
import dev.tindersamurai.prokurator.configuration.security.props.JwtSecretProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationProcessor authenticationProcessor;
	private final JwtSecretProperties jwtSecretProperties;

	public JwtAuthenticationFilter(
			AuthenticationProcessor authenticationProcessor,
			JwtSecretProperties jwtSecretProperties,
			String filterProcessUrl
	) {
		this.jwtSecretProperties = jwtSecretProperties;
		this.authenticationProcessor = authenticationProcessor;
		setFilterProcessesUrl(filterProcessUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return authenticationProcessor.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain filterChain, Authentication authentication) {
		val user = ((User) authentication.getPrincipal());

		val roles = user.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		val signingKey = jwtSecretProperties.getJwtSecretKey().getBytes();

		val token = Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
				.setHeaderParam("typ", jwtSecretProperties.getJwtTokenType())
				.setIssuer(jwtSecretProperties.getJwtTokenIssuer())
				.setAudience(jwtSecretProperties.getJwtTokenAudience())
				.setSubject(user.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + 864000000))
				.claim("rol", roles)
				.compact();

		response.addHeader(
				jwtSecretProperties.getJwtTokenHeader(),
				jwtSecretProperties.getJwtTokenPrefix() + token
		);
	}
}
