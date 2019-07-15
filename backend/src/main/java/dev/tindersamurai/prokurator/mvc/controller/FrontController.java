package dev.tindersamurai.prokurator.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j @Controller
@RequestMapping("/")
public class FrontController {

	@GetMapping
	public String index() {
		return "index.html";
	}

	@GetMapping("/main")
	public String main() {
		return "redirect:/";
	}

	@GetMapping("/home")
	public String home() {
		return main();
	}

	@GetMapping("/root")
	public String root() {
		return home();
	}

}
