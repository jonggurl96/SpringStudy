package com.demo.spring.config.security.provider;


import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.config.security.exception.PasswordNotMatchException;
import com.demo.spring.config.security.util.mng.RsaAesManager;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
public class JpaDaoAuthProvider extends DaoAuthenticationProvider {
	
	private final RsaAesProperties rsaAesProperties;
	
	/**
	 * 로그인 실패 횟수 정의
	 */
	@Value("${login.maxTryNo}")
	private int MAX_CO;
	
	public JpaDaoAuthProvider(UserDetailsService userDetailsService,
	                          BCryptPasswordEncoder bCryptPasswordEncoder,
	                          RsaAesProperties rsaAesProperties) {
		super(bCryptPasswordEncoder);
		setUserDetailsService(userDetailsService);
		this.rsaAesProperties = rsaAesProperties;
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	protected void additionalAuthenticationChecks(UserDetails userDetails,
	                                              UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
		HashMap<String, Object> credentials = (HashMap<String, Object>) authentication.getCredentials();
		
		HttpServletRequest req =
				((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		
		RsaAesManager manager = (RsaAesManager) req.getSession().getAttribute(rsaAesProperties.getSessionKey());
		String decodedPassword = manager.decryptAES(customUserDetails.getPassword());
		log.debug(">>> Decoded Password. {}", decodedPassword);
		
		PasswordEncoder encoder = getPasswordEncoder();
		if(!encoder.matches((CharSequence) credentials.get("password"), decodedPassword)) {
			int userCo = 1; // TODO tn_users 테이블에 칼럼 추가
			throw new PasswordNotMatchException(String.format("%d/%d", userCo, MAX_CO));
		}
	}
	
}
