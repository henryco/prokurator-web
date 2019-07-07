package dev.tindersamurai.prokurator.configuration.swagger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile({"dev", "develop"})
public class SwaggerApiController {

	@RequestMapping("/swagger") public String home() {
		return "redirect:/swagger-ui.html";
	}
}
