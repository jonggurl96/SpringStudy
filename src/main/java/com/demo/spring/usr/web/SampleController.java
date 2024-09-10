package com.demo.spring.usr.web;


import com.demo.spring.config.security.annotation.AuthenticatedUser;
import com.demo.spring.sec.role.usr.repository.UserAuthorRepository;
import com.demo.spring.usr.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {
	
	private final UserAuthorRepository userAuthorRepository;
	
	@PostMapping("/api/a/text")
	public String textA(@AuthenticatedUser UserDTO userDTO) {
		String authorities = userAuthorRepository.getAuthorityString(userDTO.getUserNo());
		return "/api/a/text requested.\n" + authorities;
	}
	
	@PostMapping("/api/b/text")
	public String textB(@AuthenticatedUser UserDTO userDTO) {
		String authorities = userAuthorRepository.getAuthorityString(userDTO.getUserNo());
		return "/api/b/text requested.\n" + authorities;
	}
}
