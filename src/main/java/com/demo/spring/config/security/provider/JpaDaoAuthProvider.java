package com.demo.spring.config.security.provider;


import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.config.security.exception.PasswordNotMatchException;
import com.demo.spring.config.security.service.UserDetailsServiceImpl;
import com.demo.spring.usr.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

@Slf4j
public class JpaDaoAuthProvider extends DaoAuthenticationProvider {
	
	/**
	 * 로그인 실패 횟수 정의
	 */
	@Value("${login.maxTryNo}")
	private int MAX_CO;
	
	public JpaDaoAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		super(passwordEncoder);
		setUserDetailsService(userDetailsService);
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	protected void additionalAuthenticationChecks(UserDetails userDetails,
	                                              UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
		HashMap<String, Object> credentials = (HashMap<String, Object>) authentication.getCredentials();
		
		String decodedPassword = (String) credentials.get("password");
		log.debug(">>> Decoded Password. {}", decodedPassword);
		
		UserDTO userDTO = customUserDetails.getUserDTO();
		
		PasswordEncoder encoder = getPasswordEncoder();
		if(!encoder.matches(decodedPassword, customUserDetails.getPassword())) {
			
			getService().increaseLoginFailrCnt(userDTO);
			if(customUserDetails.isExceedLoginFailrCnt(MAX_CO))
				throw new PasswordNotMatchException(customUserDetails.getLoginFailrCnt(), MAX_CO);
		}
		else getService().initLoginFailrCnt(userDTO);
	}
	
	private UserDetailsServiceImpl getService() {
		return (UserDetailsServiceImpl) getUserDetailsService();
	}
	
}
