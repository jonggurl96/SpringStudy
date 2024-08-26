package com.demo.spring.config.security.exception.dec;


import org.springframework.security.core.AuthenticationException;

/**
 * CryptoException.java
 * <pre>
 * Decryption Failed Exception
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 26.
 */
public class CryptoException extends AuthenticationException {
	
	public CryptoException(String msg) {
		super(msg);
	}
	
	public CryptoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
