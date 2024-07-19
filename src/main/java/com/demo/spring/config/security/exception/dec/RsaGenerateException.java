package com.demo.spring.config.security.exception.dec;

/**
 * RsaGenerateException.java
 * <pre>
 * Record RSAVO 생성 실패
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 18.
 */
public class RsaGenerateException extends RuntimeException {
	
	public RsaGenerateException(String msg) {
		super(msg);
	}
	
	public RsaGenerateException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
