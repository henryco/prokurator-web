package dev.tindersamurai.prokurator.mvc.controller.api.open;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController @RequestMapping("/api/open")
public class TestOpenedController {

	@GetMapping("/ping")
	public String pingPong(Principal authentication) {
		log.debug("auth: {}", authentication);
		return "pong: " + authentication;
	}
}
