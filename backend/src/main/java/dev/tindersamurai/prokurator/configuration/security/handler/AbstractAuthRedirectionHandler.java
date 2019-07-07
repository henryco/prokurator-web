package dev.tindersamurai.prokurator.configuration.security.handler;

import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthRedirectionHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException, ServletException {

		setDefaultTargetUrl(resolveTargetUrl(request, response, authentication));

		val targetUrl = determineTargetUrl(request, response);
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
		super.clearAuthenticationAttributes(request);
	}

	public abstract String resolveTargetUrl(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException, ServletException;

}
