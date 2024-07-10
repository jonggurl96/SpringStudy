package com.demo.spring.usr.web;


import com.demo.spring.config.security.annotation.AuthenticationUser;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.web.AbstractController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController extends AbstractController {
	
	@PostMapping("/login")
	public String login() throws Exception {
		return "/main";
	}
	
	@GetMapping("/main")
	public String mainPage(@AuthenticationUser UserDTO userDTO) throws Exception {
		log.debug(">>> main. {}", userDTO);
		return "/main";
	}
}
