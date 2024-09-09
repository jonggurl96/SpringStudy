package com.demo.spring.config.security.provider;


import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.config.security.exception.LoginFailedTooMuchException;
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

import java.util.Map;

@Slf4j
public class JpaDaoAuthProvider extends DaoAuthenticationProvider {
	
	/**
	 * 로그인 실패 횟수 정의
	 */
	@Value("${login.maxTryNo}")
	private int MAX_CO;
	
	public JpaDaoAuthProvider(UserDetailsService userDetailsService,
	                          PasswordEncoder passwordEncoder) {
		super(passwordEncoder);
		setUserDetailsService(userDetailsService);
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	protected void additionalAuthenticationChecks(UserDetails userDetails,
	                                              UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
		Map<String, String> credentials = (Map<String, String>) authentication.getCredentials();
		
		String inputPwd = credentials.get("password");
		log.debug(">>> Password User Input. {}", inputPwd);
		
		UserDTO userDTO = customUserDetails.getUserDTO();
		
		if(!getPasswordEncoder().matches(inputPwd, customUserDetails.getPassword())) {
			
			getService().increaseLoginFailrCnt(userDTO);
			int increasedCnt = customUserDetails.getLoginFailrCnt() + 1;
			if(customUserDetails.isExceedLoginFailrCnt(MAX_CO))
				throw new LoginFailedTooMuchException("로그인 시도 횟수 초과");
			
			throw new PasswordNotMatchException(increasedCnt, MAX_CO);
		}
		else {
			getService().initLoginFailrCnt(userDTO);
		}
	}
	
	private UserDetailsServiceImpl getService() {
		return (UserDetailsServiceImpl) getUserDetailsService();
	}
	
}
