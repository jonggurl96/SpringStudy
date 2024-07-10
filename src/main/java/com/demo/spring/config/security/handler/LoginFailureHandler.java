package com.demo.spring.config.security.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@Setter
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private String forwardUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException exception) throws IOException, ServletException {
		String username = request.getParameter("username");
		log.error(">>> {} Login Failed.", username);
		request.getRequestDispatcher(forwardUrl).forward(request, response);
	}
	
}
