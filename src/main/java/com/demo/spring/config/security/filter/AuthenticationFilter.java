package com.demo.spring.config.security.filter;


import com.demo.spring.config.security.util.helper.AESGenHelper;
import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.vo.AESVO;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AESGenHelper aesGenHelper;
	
	private final RSAGenHelper rsaGenHelper;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = obtainUsername(request);
		if(username == null || username.isBlank())
			username = "";
		username = username.trim();
		
		Map<String, Object> credentials = new HashMap<>();
		String password = obtainPassword(request);
		password = password == null ? "" : password;
		
		String decodedPassword = decrypt(request, password);
		log.debug(">>> Decoded Password. {}", decodedPassword);
		credentials.put("password", decodedPassword);
		
		log.debug(">>> username: {}, password: {}", username, password);
		
		UsernamePasswordAuthenticationToken authRequst = new UsernamePasswordAuthenticationToken(username,
		                                                                                         credentials);
		setDetails(request, authRequst);
		log.debug(">>> authRequest: {}", authRequst);
		return getAuthenticationManager().authenticate(authRequst);
	}
	
	private String decrypt(HttpServletRequest request, String encrypted) {
		String encryptedKey = request.getParameter("encryptedKey");
		String iv = request.getParameter("iv");
		
		RSAVO rsavo = rsaGenHelper.getSessionAttr(request.getSession());
		String aesKey = rsavo.decrypt(encryptedKey);
		
		AESVO aesvo = AESVO.importVO(aesKey, iv);
		aesGenHelper.setWebAttr(aesvo, request.getSession());
		return aesvo.decrypt(encrypted);
	}
	
}
