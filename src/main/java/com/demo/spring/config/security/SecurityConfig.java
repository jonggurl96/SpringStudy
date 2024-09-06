package com.demo.spring.config.security;


import com.demo.spring.config.security.encoder.AesEncoder;
import com.demo.spring.config.security.filter.AuthenticationFilter;
import com.demo.spring.config.security.handler.*;
import com.demo.spring.config.security.provider.JpaDaoAuthProvider;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	
	private final RsaAesProperties rsaAesProperties;
	
	@Bean
	public AesEncoder aesEncoder() {
		return new AesEncoder(rsaAesProperties);
	}
	
	@Bean
	public JpaDaoAuthProvider provider() {
		return new JpaDaoAuthProvider(userDetailsService, aesEncoder());
	}
	
	public ProviderManager providerManager() {
		return new ProviderManager(List.of(provider()));
	}
	
	public AuthenticationFilter authenticationFilter() throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter(rsaAesProperties);
		filter.setFilterProcessesUrl("/actionLogin");
		filter.setAuthenticationManager(providerManager());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler("/main"));
		filter.setAuthenticationFailureHandler(new LoginFailureHandler("/loginError"));
		return filter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// CSRF 방지
		http.csrf(AbstractHttpConfigurer::disable);
		
		// 기본적인 http 로그인 방식 미사용
		http.httpBasic(AbstractHttpConfigurer::disable);
		
		http.formLogin(page -> page.loginPage("/login")
		                           .loginProcessingUrl("/actionLogin")
		                           .defaultSuccessUrl("/main", true)
		                           .usernameParameter("username")
		                           .passwordParameter("password")
		                           .permitAll());
		
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers("/crypto/**", "/loginError**").permitAll()
				.requestMatchers("/properties/**").hasRole("ADMIN")
				.requestMatchers("/api/a/**").hasRole("A")
				.requestMatchers("/api/b/**").hasRole("B")
				.anyRequest().authenticated());
		
		http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthEntryPoint("/login"))
		                                         .accessDeniedHandler(new CustomAcessDeniedHandler()));
		
		http.logout(logout -> logout
				.logoutUrl("/logout")
				.permitAll()
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.logoutSuccessHandler(new LogoutSuccessHandler("/login")));
		
		http.sessionManagement(configurer -> configurer
				.sessionFixation()
				.changeSessionId()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
}
