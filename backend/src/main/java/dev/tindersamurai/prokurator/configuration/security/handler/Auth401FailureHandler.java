package dev.tindersamurai.prokurator.configuration.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

@Slf4j @Component
public class Auth401FailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception
	) throws IOException {
		val data = new HashMap<String, Object>(); {
			data.put("timestamp", Calendar.getInstance().getTime());
			data.put("exception", exception.getMessage());
		}
		log.debug("EX MAP: {} | {}", exception, data);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(new ObjectMapper().writeValueAsString(data));
	}

}
