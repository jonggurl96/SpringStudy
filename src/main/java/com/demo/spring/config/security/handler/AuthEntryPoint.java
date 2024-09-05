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
 * - 인증되지 않은 사용자에게 어디에서 인증을 수행해야 하는지 알려주는 역할
 * - 로그인 페이지로 리다이렉트하거나 인증을 위한 다른 경로 제공
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 17.
 */
@Slf4j
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
	
	private final String loginPage;
	
	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException) throws IOException, ServletException {
		log.debug(">>> 인증되지 않은 사용자 접근. 로그인 페이지로 이동");
		response.sendRedirect(loginPage);
	}
	
}
