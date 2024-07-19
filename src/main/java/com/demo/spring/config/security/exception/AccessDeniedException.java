package com.demo.spring.config.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * AccessDeniedException.java
 * <pre>
 * 접근 불가
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 16.
 */
public class AccessDeniedException extends AuthenticationException {
	
	public AccessDeniedException(String msg) {
		super(msg);
	}
	
	public AccessDeniedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
