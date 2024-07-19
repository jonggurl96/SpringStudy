package com.demo.spring.config.security.util.properties;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecoderProperties {
	
	private int keySize;
	
	private int radixModulus;
	
	private int radixExponent;
	
	private String attrMod;
	
	private String attrExp;
	
	private String attrKey;
	
}
