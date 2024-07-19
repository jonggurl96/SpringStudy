package com.demo.spring.config.security.filter;


import com.demo.spring.config.security.exception.PasswordNotMatchException;
import com.demo.spring.config.security.util.helper.DecoderGenHelper;
import com.demo.spring.config.security.util.vo.DecoderVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final DecoderGenHelper<? extends DecoderVO> genHelper;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = obtainUsername(request);
		if(username == null || username.isBlank())
			username = "";
		username = username.trim();
		
		Map<String, Object> credentials = new HashMap<>();
		String password = obtainPassword(request);
		credentials.put("password", decrypt(password));
		
		log.debug(">>> username: {}, password: {}", username, password);
		
		UsernamePasswordAuthenticationToken authRequst = new UsernamePasswordAuthenticationToken(username, credentials);
		setDetails(request, authRequst);
		log.debug(">>> authRequest: {}", authRequst);
		return getAuthenticationManager().authenticate(authRequst);
	}
	
	private String decrypt(String encrypted) {
		DecoderVO decoder = genHelper.generate();
		try {
			return decoder.decrypt(encrypted);
		} catch(GeneralSecurityException e) {
			throw new PasswordNotMatchException("Password Decode Error.", e);
		}
	}
	
}
