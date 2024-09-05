package com.demo.spring.config.jwt.provider;


import com.demo.spring.config.jwt.exception.InvalidTokenException;
import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.usr.dto.UserDTO;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JwtProvider.java
 * <pre>
 * <b>기능</b>
 * - 1. 로그인 성공 시 request header에 토큰 생성 후 저장
 * - 2. 토큰 validate
 * - 2-1. 토큰 정상: return true
 * - 2-2. 토큰 만료: update 후 return true
 * - 2-3. 그 외 에러: exception
 *
 * <b>암호화 알고리즘</b>
 * <i>RSA256</i>
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 18.
 */
@Slf4j
@Getter
@Component
public class JwtProvider {
	
	/**
	 * Default: 10 Min.
	 */
	@Value("#{${jwt.lifetime.minute:10} * 60 * 1000}")
	private int LIFETIME;
	
	@Value("#{'${jwt.token.prefix}' + ' '}")
	private String PREFIX;
	
	@Value("${jwt.token.header}")
	private String HEADER;
	
	private final PrivateKey PRIVATE_KEY;
	
	private final PublicKey PUBLIC_KEY;
	
	private static final String CHAINING = "||>";
	
	private static final String AUTHORITIES = "authorities";
	
	private static final String IP = "IP_Address";
	
	private static final String SESSIONID = "SESSION_ID";
	
	public JwtProvider() {
		KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
		PRIVATE_KEY = keyPair.getPrivate();
		PUBLIC_KEY = keyPair.getPublic();
	}
	
	public String fromAuthentication(Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		UserDTO userDTO = customUserDetails.getUserDTO();
		String authorities = customUserDetails.getAuthorities()
		                                      .stream()
		                                      .map(Object::toString)
		                                      .sorted()
		                                      .collect(Collectors.joining(CHAINING));
		
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		
		return PREFIX + getBuilder(userDTO.getUserNo())
				.claim(AUTHORITIES, authorities)
				.claim(IP, details.getRemoteAddress())
				.claim(SESSIONID, details.getSessionId())
				.compact();
	}
	
	public String update(String accessToken,
	                     Authentication authentication) throws InvalidTokenException {
		
		String parsedToken = accessToken.replace(PREFIX, "");
		
		try {
			Claims claims = Jwts.parser()
			                    .verifyWith(PUBLIC_KEY)
			                    .build()
			                    .parseSignedClaims(parsedToken)
			                    .getPayload();
			if(validateUserDetails(claims, authentication))
				return accessToken;
			return fromAuthentication(authentication);
			
		} catch(ExpiredJwtException ignored) {
			log.error(">>> Expired Token: {}", accessToken);
			
			String updateToken = fromAuthentication(authentication);
			log.debug(">>> UPdated Token: {}", updateToken);
			
			return updateToken;
			
		} catch(UnsupportedJwtException ignored) {
			throw new InvalidTokenException("Unsupported Token: " + accessToken);
		} catch(MalformedJwtException ignored) {
			throw new InvalidTokenException("Malformed Token: " + accessToken);
		} catch(IllegalArgumentException ignored) {
			throw new InvalidTokenException("Illegal Token: " + accessToken);
		} catch(RuntimeException rte) {
			throw new InvalidTokenException("Other Exception", rte);
		}
	}
	
	private Date getNow() {
		return new Date();
	}
	
	private Date getExpired(Date generatedNow) {
		return new Date(generatedNow.getTime() + LIFETIME);
	}
	
	private JwtBuilder getBuilder(String userNo) {
		Date now = getNow();
		return Jwts.builder()
		           .subject(userNo)
		           .issuedAt(now)
		           .expiration(getExpired(now))
		           .signWith(PRIVATE_KEY);
	}
	
	private boolean validateUser(Claims claims, UserDTO userDTO) {
		if(!claims.getSubject().equals(userDTO.getUserNo()))
			return false;
		return true;
	}
	
	private boolean validateUserDetails(Claims claims, Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		if(!validateUser(claims, customUserDetails.getUserDTO()))
			return false;
		
		return checkIP(claims, details)
		       && checkSession(claims, details)
		       && checkAuthorities(claims, customUserDetails);
	}
	
	private boolean checkAuthorities(Claims claims, CustomUserDetails customUserDetails) {
		return customUserDetails.getAuthorities().stream()
		                        .map(Object::toString)
		                        .sorted()
		                        .collect(Collectors.joining(CHAINING))
		                        .equals(claims.get(AUTHORITIES));
	}
	
	private boolean checkIP(Claims claims, WebAuthenticationDetails details) {
		return claims.get(IP, String.class).equals(details.getRemoteAddress());
	}
	
	private boolean checkSession(Claims claims, WebAuthenticationDetails details) {
		return claims.get(SESSIONID, String.class).equals(details.getSessionId());
	}
	
}
