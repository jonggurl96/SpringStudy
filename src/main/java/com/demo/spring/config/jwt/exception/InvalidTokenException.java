package com.demo.spring.config.jwt.exception;


import io.jsonwebtoken.JwtException;
/**
 * InvalidTokenException.java
 * <pre>
 * Throw when JWT token validate fail.
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 18.
 */
public class InvalidTokenException extends JwtException {
	
	public InvalidTokenException(String msg) {
		super(msg);
	}
	
	public InvalidTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
