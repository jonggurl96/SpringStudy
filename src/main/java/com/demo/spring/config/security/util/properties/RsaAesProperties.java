package com.demo.spring.config.security.util.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "decoder.rsaes")
public class RsaAesProperties {
	
	private String rsaName;
	
	private String rsaMd;
	
	private String rsaMgf;
	
	private int rsaKeySize;
	
	private String aesName;
	
	private String aesKey;
	
	private int aesKeySize;
	
	private String sessionKey = "RSA_AES_MANAGER";
	
}
