package com.demo.spring.config.security.util.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "decoder.rsa")
public class RSAProperties extends DecoderProperties {
}
