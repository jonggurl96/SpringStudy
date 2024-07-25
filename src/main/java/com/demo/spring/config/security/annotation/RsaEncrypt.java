package com.demo.spring.config.security.annotation;


import com.demo.spring.config.security.util.helper.RSAGenHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@PubPriKeyEncrypt(algorithm = "RSA", helperClass = RSAGenHelper.class)
public @interface RsaEncrypt {
}
