package com.demo.spring.config.security.provider;


import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.config.security.exception.PasswordNotMatchException;
import com.demo.spring.sec.crypto.EncUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

@Slf4j
public class JpaDaoAuthProvider extends DaoAuthenticationProvider {
	
	private final EncUtil encUtil;
	
	/**
	 * 로그인 실패 횟수 정의
	 */
	@Value("${login.maxTryNo}")
	private int MAX_CO;
	
	public JpaDaoAuthProvider(UserDetailsService userDetailsService, EncUtil encUtil,
	                          BCryptPasswordEncoder bCryptPasswordEncoder) {
		super(bCryptPasswordEncoder);
		setUserDetailsService(userDetailsService);
		this.encUtil = encUtil;
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	protected void additionalAuthenticationChecks(UserDetails userDetails,
	                                              UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
		HashMap<String, Object> credentials = (HashMap<String, Object>) authentication.getCredentials();
		
		PasswordEncoder encoder = getPasswordEncoder();
		String decodedPassword = encUtil.decrypt(customUserDetails.getPassword());
		log.debug(">>> Decoded Password. {}", decodedPassword);
		if(!encoder.matches((CharSequence) credentials.get("password"), decodedPassword)) {
			int userCo = 1; // tn_users 테이블에 칼럼 추가
			throw new PasswordNotMatchException(String.format("%d/%d", userCo, MAX_CO));
		}
	}
	
}
