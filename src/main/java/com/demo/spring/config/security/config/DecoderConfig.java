package com.demo.spring.config.security.config;


import com.demo.spring.config.security.util.helper.AESGenHelper;
import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.properties.AESProperties;
import com.demo.spring.config.security.util.properties.RSAProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Setter
@PropertySource("classpath:/static/properties/decoder.yml")
@Configuration
@RequiredArgsConstructor
public class DecoderConfig {
	
	private final RSAProperties rsaProperties;
	
	private final AESProperties aesProperties;
	
	@Bean
	public RSAGenHelper rsaGenHelper() {
		return new RSAGenHelper(rsaProperties);
	}
	
	@Bean
	public AESGenHelper aesGenHelper() {
		return new AESGenHelper(aesProperties);
	}
	
}
