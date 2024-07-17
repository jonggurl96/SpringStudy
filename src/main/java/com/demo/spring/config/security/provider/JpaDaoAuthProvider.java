package com.demo.spring.config.security.provider;


import com.demo.spring.config.excptn.exception.PasswordNotMatchException;
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
		HashMap<String, Object> credentials = (HashMap<String, Object>) authentication.getCredentials();
		
		if(!credentials.get("password").equals(customUserDetails.getPassword())) {
			int maxCo = 5; // yml 파일에서 로그인 실패 횟수 정의
			int userCo = 1; // tn_users 테이블에 칼럼 추가
			throw new PasswordNotMatchException(String.format("%d/%d", userCo, maxCo));
		}
	}
	
}
