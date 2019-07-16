package dev.tindersamurai.prokurator.mvc.controller.api.closed;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController @RequestMapping("/api/protected")
public class TestSecuredController {

	@GetMapping("/ping")
	public String pingPong(Authentication authentication) {
		return "pong: " + authentication.getName() + " : " + authentication;
	}
}
