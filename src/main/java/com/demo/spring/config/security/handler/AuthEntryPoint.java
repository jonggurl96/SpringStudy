package com.demo.spring.config.security.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * AuthEntryPoint.java
 * <pre>
 * 인증 실패 시 리디렉션 point 지정
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 17.
 */
@Slf4j
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
	
	private final String targetUrl;
	
	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException) throws IOException, ServletException {
		log.error(">>> AuthEntryPoint sendRedirect(\"{}\")", targetUrl, authException);
		response.sendRedirect(targetUrl);
	}
	
}
