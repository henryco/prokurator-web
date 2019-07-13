package dev.tindersamurai.prokurator.configuration.security.filter.jwt;

import dev.tindersamurai.prokurator.configuration.security.filter.jwt.props.JwtSecretProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private final JwtSecretProperties jwtSecretProperties;

	public JwtAuthorizationFilter(
			AuthenticationManager authenticationManager,
			JwtSecretProperties jwtSecretProperties
	) {
		super(authenticationManager);
		this.jwtSecretProperties = jwtSecretProperties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws IOException, ServletException {
		val authentication = getAuthentication(request);
		if (authentication == null) {
			filterChain.doFilter(request, response);
			return;
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		val token = request.getHeader(jwtSecretProperties.getJwtTokenHeader());
		if (StringUtils.isNotEmpty(token) && token.startsWith(jwtSecretProperties.getJwtTokenPrefix())) {
			try {
				val signingKey = jwtSecretProperties.getJwtSecretKey().getBytes();

				val parsedToken = Jwts.parser()
						.setSigningKey(signingKey)
						.parseClaimsJws(token.replace("Bearer ", ""));

				val username = parsedToken
						.getBody()
						.getSubject();

				val authorities = ((List<?>) parsedToken.getBody()
						.get("rol")).stream()
						.map(authority -> new SimpleGrantedAuthority((String) authority))
						.collect(Collectors.toList());

				if (StringUtils.isNotEmpty(username)) {
					return new UsernamePasswordAuthenticationToken(username, null, authorities);
				}
			} catch (ExpiredJwtException e) {
				log.warn("Request to parse expired JWT : {} failed : {}", token, e.getMessage());
			} catch (UnsupportedJwtException e) {
				log.warn("Request to parse unsupported JWT : {} failed : {}", token, e.getMessage());
			} catch (MalformedJwtException e) {
				log.warn("Request to parse invalid JWT : {} failed : {}", token, e.getMessage());
			} catch (SignatureException e) {
				log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, e.getMessage());
			} catch (IllegalArgumentException e) {
				log.warn("Request to parse empty or null JWT : {} failed : {}", token, e.getMessage());
			}
		}

		return null;
	}
}
