package com.demo.spring.config.jwt.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

/**
 * RequestHeaderWrapper.java
 * <pre>
 * request header값을 전달하기위한 wrapper
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 18.
 */
public class RequestHeaderWrapper extends HttpServletRequestWrapper {
	
	private HashMap<String, String> headerMap = new HashMap<>(2);
	
	public RequestHeaderWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public RequestHeaderWrapper addHeader(String key, String value) {
		headerMap.put(key, value);
		return this;
	}
	
	@Override
	public String getHeader(String name) {
		return headerMap.containsKey(name) ? headerMap.get(name) : super.getHeader(name);
	}
	
	@Override
	public Enumeration<String> getHeaderNames() {
		Set<String> keySet =  headerMap.keySet();
		keySet.addAll(Collections.list(super.getHeaderNames()));
		return Collections.enumeration(keySet);
	}
	
}
