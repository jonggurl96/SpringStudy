package com.demo.spring.config.security.exception;


import lombok.Getter;
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
@Getter
public class PasswordNotMatchException extends AuthenticationException {
	
	private int tryCnt;
	
	private int maxTryCnt;
	
	public PasswordNotMatchException(int tryCnt, int maxTryCnt) {
		super("Password Not Match. Try: " + tryCnt + ", maxTryCnt: " + maxTryCnt);
		this.tryCnt = tryCnt;
		this.maxTryCnt = maxTryCnt;
	}
	
	public PasswordNotMatchException(String msg) {
		super(msg);
	}
	
	public PasswordNotMatchException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
