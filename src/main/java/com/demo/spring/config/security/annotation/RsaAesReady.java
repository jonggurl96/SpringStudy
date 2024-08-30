package com.demo.spring.config.security.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RsaAesReady.java
 * <pre>
 * AES 대칭키 암호화 사용을 위해 키값을 RSA 암호화
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @see com.demo.spring.config.security.util.mng.RsaAesManager
 * @since 24. 7. 30.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@PubPriKeyEncrypt(algorithm = "RSAAES")
public @interface RsaAesReady {
}
