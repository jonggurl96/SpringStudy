package com.demo.spring.config.security.provider;


import com.demo.spring.config.security.auth.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;

public class JpaDaoAuthProvider extends DaoAuthenticationProvider {
	
	public JpaDaoAuthProvider(UserDetailsService userDetailsService) {
		super();
		setUserDetailsService(userDetailsService);
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
		String username = (String) authentication.getPrincipal();
		HashMap<String, Object> credentials = (HashMap<String, Object>) authentication.getCredentials();
		
		if(!customUserDetails.getUserDTO().getUserId().equals(username))
			throw new RuntimeException("로그인 정보 불일치");
		
//		if(!credentials.get("password").equals(customUserDetails.getPassword()))
//			throw new RuntimeException("비밀번호 불일치");
	}
	
}
