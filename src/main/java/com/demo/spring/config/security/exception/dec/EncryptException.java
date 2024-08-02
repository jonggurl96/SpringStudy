package com.demo.spring.config.security.exception.dec;


import org.springframework.security.core.AuthenticationException;

/**
 * EncryptException.java
 * <pre>
 * Encryption Failed Exception
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 8. 1.
 */
public class EncryptException extends AuthenticationException {
	
	public EncryptException(String msg) {
		super(msg);
	}
	
	public EncryptException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
