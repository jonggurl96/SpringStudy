package com.demo.spring.config.security.web;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CryptoController {
	
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/crypto")
	public String cryptoPage() {
		return "/crypto/index";
	}
	
	@GetMapping("/crypto/{text}")
	@ResponseBody
	public String encryptText(@PathVariable String text) {
		return passwordEncoder.encode(text);
	}
	
}
