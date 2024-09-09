package com.demo.spring.config.security.filter;


import com.demo.spring.config.security.util.mng.RsaAesManager;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import com.demo.spring.config.security.vo.CipherTextVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.HashMap;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final RsaAesProperties rsaAesProperties;
	
	public AuthenticationFilter(RsaAesProperties rsaAesProperties) {
		super();
		this.rsaAesProperties = rsaAesProperties;
		
		super.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = obtainUsername(request);
		username = username == null ? "" : username.trim();
		
		String password = obtainPassword(request);
		password = password == null ? "" : password;
		
		CipherTextVO pwdCipherText = decrypt(request, password);
		
		log.debug(">>> username: {}, password: {}", username, pwdCipherText.cipherText());
		
		HashMap<String, String> credentials = HashMap.newHashMap(8);
		credentials.put("password", pwdCipherText.text());
		
		UsernamePasswordAuthenticationToken authRequst = new UsernamePasswordAuthenticationToken(username,
		                                                                                         credentials);
		setDetails(request, authRequst);
		log.debug(">>> authRequest: {}", authRequst);
		
		return getAuthenticationManager().authenticate(authRequst);
	}
	
	private CipherTextVO decrypt(HttpServletRequest request, String text) {
		HttpSession session = request.getSession();
		RsaAesManager manager = (RsaAesManager) session.getAttribute(rsaAesProperties.getSessionKey());
		
		return manager.decrypt(text);
	}
	
}
