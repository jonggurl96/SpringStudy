package com.demo.spring.config.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * NotConfirmedException.java
 * <pre>
 * 이메일 또는 휴대폰 등의 인증이 완료되지 않음
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 16.
 */
public class NotConfirmedException extends AuthenticationException {
	
	public NotConfirmedException(String msg) {
		super(msg);
	}
	
	public NotConfirmedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
