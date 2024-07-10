package com.demo.spring.config.security;


import com.demo.spring.config.security.filter.AuthenticationFilter;
import com.demo.spring.config.security.handler.AuthEntryPoint;
import com.demo.spring.config.security.handler.LoginFailureHandler;
import com.demo.spring.config.security.handler.LoginSuccessHandler;
import com.demo.spring.config.security.provider.JpaDaoAuthProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
	private final UserDetailsService userDetailsService;
	
	@Bean
	public JpaDaoAuthProvider jpaDaoAuthProvider() {
		return new JpaDaoAuthProvider(userDetailsService);
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(jpaDaoAuthProvider())
				.build();
	}
	
	@Bean
	public AuthenticationFilter authenticationFilter(HttpSecurity http) throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter();
		filter.setFilterProcessesUrl("/login");
		filter.setAuthenticationManager(authenticationManager(http));
		filter.setAuthenticationSuccessHandler(loginSuccessHandler());
		filter.setAuthenticationFailureHandler(loginFailureHandler());
		return filter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(page -> page.loginPage("/login").permitAll());
		http.authorizeHttpRequests(requests -> requests.requestMatchers(new AntPathRequestMatcher("/**"))
				.permitAll());
		http.addFilterBefore(authenticationFilter(http), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthEntryPoint()));
		return http.build();
	}
	
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler("/main");
	}
	
	@Bean
	public LoginFailureHandler loginFailureHandler() {
		LoginFailureHandler loginFailureHandler = new LoginFailureHandler();
		loginFailureHandler.setForwardUrl("/");
		return loginFailureHandler;
	}
	
}
