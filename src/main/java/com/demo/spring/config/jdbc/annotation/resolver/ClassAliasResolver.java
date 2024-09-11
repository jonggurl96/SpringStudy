package com.demo.spring.config.jdbc.annotation.resolver;


import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
/**
 * ClassAliasResolver.java
 * <pre>
 * @ClassAlias Resolver
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @see com.demo.spring.config.jdbc.annotation.ClassAlias
 * @since 24. 9. 11.
 */
@Component
public class ClassAliasResolver implements HandlerMethodArgumentResolver {
	
	@Override
	public boolean supportsParameter(@NonNull MethodParameter parameter) {
		return false;
	}
	
	@Override
	public Object resolveArgument(@NonNull MethodParameter parameter,
	                              ModelAndViewContainer mavContainer,
	                              @NonNull NativeWebRequest webRequest,
	                              WebDataBinderFactory binderFactory) throws Exception {
		return null;
	}
	
}
