package com.demo.spring.config.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * PasswordNotMatchException.java
 * <pre>
 * 비밀번호 불일치
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 16.
 */
public class PasswordNotMatchException extends AuthenticationException {
	
	public PasswordNotMatchException(String msg) {
		super(msg);
	}
	
	public PasswordNotMatchException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
