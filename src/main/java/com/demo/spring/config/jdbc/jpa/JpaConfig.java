package com.demo.spring.config.jdbc.jpa;


import com.demo.spring.config.jdbc.jpa.util.JpaSortGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {
	
	@Bean
	public JpaSortGenerator jpaSortGenerator() {
		return new JpaSortGenerator();
	}
}
