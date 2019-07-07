package dev.tindersamurai.prokurator.configuration.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j @Component
public class AuthBasedRedirectionHandler extends AbstractAuthRedirectionHandler {

	@Override @Transactional
	public String resolveTargetUrl(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) {
		return "/#/";
	}
}
