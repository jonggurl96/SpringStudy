package com.demo.spring.config.security.annotation;


import com.demo.spring.config.security.util.helper.DecoderGenHelper;
import com.demo.spring.config.security.util.vo.DecoderVO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface PubPriKeyEncrypt {
	
	String algorithm();
	
	Class<? extends DecoderGenHelper<? extends DecoderVO>> helperClass();
	
}
