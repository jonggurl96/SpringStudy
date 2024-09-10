package com.demo.spring.usr.web;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
	
	@PostMapping("/api/a/text")
	public String textA() {
		return "/api/a/text requested.";
	}
	
	@PostMapping("/api/b/text")
	public String textB() {
		return "/api/b/text requested.";
	}
}
