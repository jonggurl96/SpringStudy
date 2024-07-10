package com.demo.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication(scanBasePackages = "com.demo.spring")
public class SpringStudyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringStudyApplication.class, args);
	}
	
}
