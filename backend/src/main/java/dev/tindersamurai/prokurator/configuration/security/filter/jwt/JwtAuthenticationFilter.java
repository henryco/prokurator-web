package dev.tindersamurai.prokurator.configuration.security.filter.jwt;

import dev.tindersamurai.prokurator.configuration.security.auth.processor.AuthenticationProcessor;
import dev.tindersamurai.prokurator.configuration.security.auth.session.WhitelistService;
import dev.tindersamurai.prokurator.configuration.security.filter.jwt.props.JwtSecretProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationProcessor authenticationProcessor;
	private final JwtSecretProperties jwtSecretProperties;
	private @Setter WhitelistService whitelistService;

	public JwtAuthenticationFilter(
			AuthenticationProcessor authenticationProcessor,
			JwtSecretProperties jwtSecretProperties,
			WhitelistService whitelistService,
			String filterProcessUrl
	) {
		this(authenticationProcessor, jwtSecretProperties, filterProcessUrl);
		this.whitelistService = whitelistService;
	}

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
		val user = ((UserDetails) authentication.getPrincipal());
		val roles = user.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		val signingKey = jwtSecretProperties.getJwtSecretKey().getBytes();
		val tokenId = UUID.randomUUID().toString();

		val token = Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
				.setHeaderParam("type", jwtSecretProperties.getJwtTokenType())
				.setId(tokenId)
				.setIssuer(jwtSecretProperties.getJwtTokenIssuer())
				.setAudience(jwtSecretProperties.getJwtTokenAudience())
				.setSubject(user.getUsername())
				.setExpiration(createExpTime())
				.claim("role", roles)
				.compact();

		response.addHeader(
				jwtSecretProperties.getJwtTokenHeader(),
				jwtSecretProperties.getJwtTokenPrefix() + token
		);

		if (whitelistService != null)
			whitelistService.addTokenToWhiteList(user.getUsername(), tokenId);
	}

	private Date createExpTime() {
		val now = System.currentTimeMillis();
		return new Date(now + jwtSecretProperties.getJwtTokenLiveTime());
	}
}
