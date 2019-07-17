package dev.tindersamurai.prokurator.mvc.controller.api.closed;

import dev.tindersamurai.prokurator.configuration.security.auth.credentials.DiscordTokenPrincipal;
import dev.tindersamurai.prokurator.configuration.security.auth.session.service.access.TokenAccessService;
import dev.tindersamurai.prokurator.mvc.service.user.UserDataService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/protected")
public class TestSecuredController {

	private final TokenAccessService tokenAccessService;
	private final UserDataService userDataService;

	@Autowired
	public TestSecuredController(
			TokenAccessService tokenAccessService,
			UserDataService userDataService
	) {
		this.tokenAccessService = tokenAccessService;
		this.userDataService = userDataService;
	}

	@GetMapping("/ping")
	public String pingPong(Authentication authentication) {
		return "pong: " + authentication.getName() + " : " + authentication;
	}
	
	@GetMapping("/user")
	public String name(Authentication authentication) throws Exception {
		val principal = (DiscordTokenPrincipal) authentication.getPrincipal();
		val token = tokenAccessService.getToken(principal.getTokenId());
		return userDataService.retrieveUserData(token.getAccess()).toString();
	}
}
