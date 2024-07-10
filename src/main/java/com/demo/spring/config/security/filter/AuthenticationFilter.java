package com.demo.spring.config.security.filter;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = obtainUsername(request);
		if(username == null || username.isBlank())
			username = "";
		username = username.trim();
		
		Map<String, Object> credentials = new HashMap<>();
		String password = obtainPassword(request);
		credentials.put("password", password);
		
		log.debug(">>> username: {}, password: {}", username, password);
		
		UsernamePasswordAuthenticationToken authRequst = new UsernamePasswordAuthenticationToken(username, credentials);
		setDetails(request, authRequst);
		log.debug(">>> authRequest: {}", authRequst);
		return getAuthenticationManager().authenticate(authRequst);
	}
	
}
