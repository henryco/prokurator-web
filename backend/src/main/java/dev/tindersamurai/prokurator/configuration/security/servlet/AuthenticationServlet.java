package dev.tindersamurai.prokurator.configuration.security.servlet;

import dev.tindersamurai.prokurator.configuration.props.secrets.ProkuratorSecrets;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@RequestMapping("/")
public class AuthenticationServlet {

	private final ProkuratorSecrets secrets;

	@Autowired
	public AuthenticationServlet(ProkuratorSecrets secrets) {
		this.secrets = secrets;
	}

	@GetMapping("/auth")
	public String auth(@RequestParam("code") String code) {
		return "redirect:/#/auth?code=" + code;
	}

	@GetMapping("/login")
	public String login() {
		val base = "https://discordapp.com/api/oauth2/authorize";
		val redirect = "redirect_uri=" + secrets.getOAuthRedirect();
		val clientId = "client_id=" + secrets.getClientId();
		val scope = "scope=" + secrets.getOAuthScope();
		val type = "response_type=code";
		return "redirect:" + base + "?" + clientId + "&" + redirect + "&" + scope + "&" + type;
	}
}
