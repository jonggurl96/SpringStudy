package com.demo.spring.config.security.config;


import com.demo.spring.config.security.util.helper.DecoderGenHelper;
import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.properties.DecoderProperties;
import com.demo.spring.config.security.util.properties.RSAProperties;
import com.demo.spring.config.security.util.vo.DecoderVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Setter
@PropertySource("classpath:/static/properties/decoder.yml")
@Configuration
public class DecoderConfig {
	
	@Value("${decoder.type}")
	private String type;
	
	@Bean
	public DecoderGenHelper<? extends DecoderVO> genHelper() {
		return switch(type) {
			case "RSA" -> rsaGenHelper();
			default -> null;
		};
	}
	
	private RSAGenHelper rsaGenHelper() {
		DecoderProperties properties = getProperties();
		return new RSAGenHelper(properties.getKeySize(), properties.getRadixModulus(), properties.getRadixExponent(), properties.getAttrKey(), properties.getAttrMod(), properties.getAttrExp());
	}
	
	@Bean
	public DecoderProperties getProperties() {
		return switch(type) {
			case "RSA" -> new RSAProperties();
			default -> null;
		};
	}
	
}
