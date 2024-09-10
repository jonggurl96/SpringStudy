package com.demo.spring.config.jwt.filter;


import com.demo.spring.config.jwt.provider.JwtProvider;
import com.demo.spring.config.jwt.util.JwtRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtProvider provider;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
	                                @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(isAnonymous(authentication)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		JwtRequestWrapper wrapper = new JwtRequestWrapper(request);
		
		String jwtToken = wrapper.getToken();
		
		jwtToken = jwtToken == null
		           ? provider.fromAuthentication(authentication)
		           : provider.update(jwtToken, authentication);
		
		wrapper.addToken(jwtToken);
		
		log.debug(">>> JWT Token: {}", wrapper.getToken());
		
		filterChain.doFilter(wrapper, response);
	}
	
	private boolean isAnonymous(Authentication authentication) {
		return authentication == null || authentication.getPrincipal().equals("anonymousUser");
	}
	
}
