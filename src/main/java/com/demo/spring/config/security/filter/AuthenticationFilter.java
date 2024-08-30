package com.demo.spring.config.security.filter;


import com.demo.spring.config.security.util.mng.RsaAesManager;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final RsaAesProperties rsaAesProperties;
	
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
		credentials.put("password", decodedPassword);
		
		log.debug(">>> username: {}, password: {}", username, password);
		
		UsernamePasswordAuthenticationToken authRequst = new UsernamePasswordAuthenticationToken(username,
		                                                                                         credentials);
		setDetails(request, authRequst);
		log.debug(">>> authRequest: {}", authRequst);
		return getAuthenticationManager().authenticate(authRequst);
	}
	
	private String decrypt(HttpServletRequest request, String text) {
		RsaAesManager manager = (RsaAesManager) request.getSession().getAttribute(rsaAesProperties.getSessionKey());
		
		String encryptedKey = request.getParameter("encryptedKey");
		
		String textChain = encryptedKey + RsaAesManager.SEPERATOR + text;
		log.debug(">>> textChain: {}", textChain);
		
		return manager.decrypt(textChain);
	}
	
	private byte[] decodeIv(String iv) {
		String hexIv = new String(Base64.getDecoder().decode(iv), StandardCharsets.UTF_8);
		int byteSize = hexIv.length() / 2;
		byte[] bytes = new byte[byteSize];
		for(int i = 0; i < byteSize; i++) {
			bytes[i] = (byte) Integer.parseInt(hexIv, i * 2, i * 2 + 2, 16);
		}
		log.debug(">>> decoded iv: {}", bytes);
		return bytes;
	}
	
}
