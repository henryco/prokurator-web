package dev.tindersamurai.prokurator.configuration.security.auth.processor;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationProcessor {

	Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response);
}
