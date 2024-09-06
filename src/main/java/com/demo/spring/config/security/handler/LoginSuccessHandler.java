package com.demo.spring.config.security.handler;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	public LoginSuccessHandler(String targetUrl) {
		super(targetUrl);
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		log.debug(">>> Authentication SUCCESS.");
		super.onAuthenticationSuccess(request, response, chain, authentication);
	}
	
}
