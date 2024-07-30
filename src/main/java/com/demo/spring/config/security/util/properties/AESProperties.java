package com.demo.spring.config.security.util.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "decoder.aes")
public class AESProperties {
	
	private String attrIv;
	
	private String attrKey;
	
	private String attrSalt;
	
	private int keySize;
	
	private String salt;
	
}
