package com.demo.spring.usr.web;


import com.demo.spring.config.security.annotation.AuthenticatedUser;
import com.demo.spring.sec.role.usr.service.mybatis.UserAuthorMybatisService;
import com.demo.spring.usr.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {
	
	private final UserAuthorMybatisService userAuthorMybatisService;
	
	@PostMapping("/api/a/text")
	public String textA(@AuthenticatedUser UserDTO userDTO) {
		String authorities = userAuthorMybatisService.getAuthorityString(userDTO.getUserNo());
		return "/api/a/text requested.\n" + authorities;
	}
	
	@PostMapping("/api/b/text")
	public String textB(@AuthenticatedUser UserDTO userDTO) {
		String authorities = userAuthorMybatisService.getAuthorityString(userDTO.getUserNo());
		return "/api/b/text requested.\n" + authorities;
	}
}
