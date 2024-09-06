package com.demo.spring.config.jwt.filter;


import com.demo.spring.config.jwt.provider.JwtProvider;
import com.demo.spring.config.jwt.util.RequestHeaderWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtProvider provider;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
	                                @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null)
			authentication = new AnonymousAuthenticationToken("anonymousUser",
			                                                  "anonymousUser",
			                                                  List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
		
		RequestHeaderWrapper wrapper = new RequestHeaderWrapper(request);
		String header = provider.getHEADER();
		
		String jwtToken = wrapper.getHeader(header);
		
		jwtToken = jwtToken == null
		           ? (isAnonymous(authentication) ? null : provider.fromAuthentication(authentication))
		           : provider.update(jwtToken, authentication);
		
		wrapper.addHeader(header, jwtToken);
		
		filterChain.doFilter(wrapper, response);
	}
	
	private boolean isAnonymous(Authentication authentication) {
		return authentication.getPrincipal().equals("anonymousUser");
	}
	
}
