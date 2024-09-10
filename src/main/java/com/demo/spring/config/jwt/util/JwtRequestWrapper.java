package com.demo.spring.config.jwt.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;

/**
 * JwtRequestWrapper.java
 * <pre>
 * request header값을 전달하기위한 wrapper
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 18.
 */
public class JwtRequestWrapper extends HttpServletRequestWrapper {
	
	private static final String HEADER = "Authorization";
	
	private HashMap<String, String> headerMap = new HashMap<>(2);
	
	public JwtRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public void addToken(String token) {
		headerMap.put(HEADER, token);
	}
	
	public String getToken() {
		return getHeader(HEADER);
	}
	
	@Override
	public String getHeader(String k) {
		return headerMap.containsKey(k) ? headerMap.get(k) : super.getHeader(k);
	}
	
}
