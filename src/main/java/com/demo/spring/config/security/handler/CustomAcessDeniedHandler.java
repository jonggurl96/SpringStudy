package com.demo.spring.config.security.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class CustomAcessDeniedHandler implements AccessDeniedHandler {
	
	@Override
	public void handle(HttpServletRequest request,
	                   HttpServletResponse response,
	                   AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.error(">>> kkk");
	}
	
}
