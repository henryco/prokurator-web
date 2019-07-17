package dev.tindersamurai.prokurator.configuration.security.filter;

import dev.tindersamurai.prokurator.configuration.security.auth.details.user.DiscordUserDetails;
import dev.tindersamurai.prokurator.configuration.security.auth.processor.AuthenticationProcessor;
import dev.tindersamurai.prokurator.configuration.security.auth.session.WhitelistService;
import dev.tindersamurai.prokurator.configuration.security.filter.props.JwtSecretProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.stream.Collectors;

import static dev.tindersamurai.prokurator.configuration.security.auth.session.WhitelistService.*;

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
		val user = ((DiscordUserDetails) authentication.getPrincipal());
		val roles = user.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		val signingKey = jwtSecretProperties.getJwtSecretKey().getBytes();

		val token = Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
				.setHeaderParam("type", jwtSecretProperties.getJwtTokenType())
				.setId(user.getTokenDetails().getTokenId())
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

		if (whitelistService != null) {
			val d = user.getTokenDetails();
			val data = new Token(d.getTokenId(), d.getAccess(), d.getRefresh(), d.getExpires());
			whitelistService.addTokenToWhiteList(user.getUsername(), data);
		}
	}

	private Date createExpTime() {
		val now = System.currentTimeMillis();
		return new Date(now + jwtSecretProperties.getJwtTokenLiveTime());
	}
}
