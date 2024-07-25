package com.demo.spring.config.security.util.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "decoder.rsa")
public class RSAProperties {
	
	private int keySize;
	
	private int radixModulus;
	
	private int radixExponent;
	
	private String attrMod;
	
	private String attrExp;
	
	private String attrKey;
	
	private String attrPub;
	
}
