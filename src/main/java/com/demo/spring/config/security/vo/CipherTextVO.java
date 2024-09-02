package com.demo.spring.config.security.vo;


/**
 * CipherTextVO.java
 * <pre>
 * 평문, 암호문 객체
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 9. 2.
 */
public record CipherTextVO(String text, String cipherText) {

}
