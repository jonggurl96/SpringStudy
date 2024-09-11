package com.demo.spring.config.jdbc.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassAlias.java
 * <pre>
 * Class마다 alias 등록
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @see com.demo.spring.config.jdbc.annotation.resolver.ClassAliasResolver
 * @since 24. 9. 11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassAlias {
	
	@AliasFor("alias")
	String value() default "";
	
	@AliasFor("value")
	String alias() default "";
	
}
