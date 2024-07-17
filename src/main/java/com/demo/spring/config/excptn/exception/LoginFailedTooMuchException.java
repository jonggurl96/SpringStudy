package com.demo.spring.config.excptn.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * LoginFailedTooMuchException.java
 * <pre>
 * 정해진 로그인 시도 최대 횟수 초과
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 16.
 */
public class LoginFailedTooMuchException extends AuthenticationException {
	
	public LoginFailedTooMuchException(String msg) {
		super(msg);
	}
	
	public LoginFailedTooMuchException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
