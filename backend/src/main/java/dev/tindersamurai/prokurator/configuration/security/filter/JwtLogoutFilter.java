package dev.tindersamurai.prokurator.configuration.security.filter;

import dev.tindersamurai.prokurator.configuration.security.auth.session.service.whitelist.TokenWhitelistService;
import dev.tindersamurai.prokurator.configuration.security.filter.props.JwtSecretProperties;
import io.jsonwebtoken.Jwts;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtLogoutFilter extends OncePerRequestFilter {

	private final JwtSecretProperties jwtSecretProperties;
	private @Setter
	TokenWhitelistService whitelistService;
	private RequestMatcher requestMatcher;

	public JwtLogoutFilter(
			JwtSecretProperties jwtSecretProperties,
			TokenWhitelistService whitelistService,
			String logoutUrl
	) {
		this(jwtSecretProperties, logoutUrl);
		this.whitelistService = whitelistService;
	}

	public JwtLogoutFilter(
			JwtSecretProperties jwtSecretProperties,
			String logoutUrl
	) {
		this.jwtSecretProperties = jwtSecretProperties;
		this.setFilterProcessesUrl(logoutUrl);

	}

	private void setRequestMatcher(
			RequestMatcher requestMatcher) {
		Assert.notNull(requestMatcher, "requestMatcher cannot be null");
		this.requestMatcher = requestMatcher;
	}

	private void setFilterProcessesUrl(String filterProcessesUrl) {
		setRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		if (!requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		log.debug("REMOVE _ TOKEN _ FROM _ WHITE _ LIST");
		removeTokenFromWhitelist(request);

		SecurityContextHolder.getContext().setAuthentication(null);
		response.setStatus(200);
	}

	private void removeTokenFromWhitelist(HttpServletRequest request) {
		val token = request.getHeader(jwtSecretProperties.getJwtTokenHeader());
		if (StringUtils.isNotEmpty(token) && token.startsWith(jwtSecretProperties.getJwtTokenPrefix())) {
			try {
				val signingKey = jwtSecretProperties.getJwtSecretKey().getBytes();
				val parsedToken = Jwts.parser()
						.setSigningKey(signingKey)
						.parseClaimsJws(token.replace(jwtSecretProperties.getJwtTokenPrefix(), ""));
				if (whitelistService != null)
					whitelistService.removeToken(parsedToken.getBody().getId());
			} catch (Exception e) {
				log.warn("Cannot parse or process jwt token", e);
			}
		}

	}

}
