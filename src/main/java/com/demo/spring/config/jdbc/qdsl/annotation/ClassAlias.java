package com.demo.spring.config.jdbc.qdsl.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassAlias.java
 * <pre>
 * Class 별칭
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
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
