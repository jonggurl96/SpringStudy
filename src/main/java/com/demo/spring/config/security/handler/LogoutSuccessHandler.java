package com.demo.spring.config.security.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	private final String targetUrl;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.debug(">>> Logout Success");
		response.sendRedirect(targetUrl);
	}
	
}
