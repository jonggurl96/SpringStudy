package com.demo.spring.config.security.exception.dec;


import org.springframework.security.core.AuthenticationException;

/**
 * DecryptException.java
 * <pre>
 * Decryption Failed Exception
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 26.
 */
public class DecryptException extends AuthenticationException {
	
	public DecryptException(String msg) {
		super(msg);
	}
	
	public DecryptException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
