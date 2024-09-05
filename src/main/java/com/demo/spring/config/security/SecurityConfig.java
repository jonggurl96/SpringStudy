package com.demo.spring.config.security;


import com.demo.spring.config.jwt.filter.JwtFilter;
import com.demo.spring.config.security.encoder.AesEncoder;
import com.demo.spring.config.security.filter.AuthenticationFilter;
import com.demo.spring.config.security.handler.AuthEntryPoint;
import com.demo.spring.config.security.handler.LoginFailureHandler;
import com.demo.spring.config.security.handler.LoginSuccessHandler;
import com.demo.spring.config.security.provider.JpaDaoAuthProvider;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	
	private final RsaAesProperties rsaAesProperties;
	
	private final JwtFilter jwtFilter;
	
	@Bean
	public AesEncoder aesEncoder() {
		return new AesEncoder(rsaAesProperties);
	}
	
	@Bean
	public JpaDaoAuthProvider jpaDaoAuthProvider() {
		return new JpaDaoAuthProvider(userDetailsService, aesEncoder());
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
		           .authenticationProvider(jpaDaoAuthProvider())
		           .build();
	}
	
	@Bean
	public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter(rsaAesProperties);
		filter.setFilterProcessesUrl("/actionLogin");
		filter.setAuthenticationManager(authenticationManager);
		filter.setAuthenticationSuccessHandler(loginSuccessHandler());
		filter.setAuthenticationFailureHandler(loginFailureHandler());
		return filter;
	}
	
	@Bean
	public AuthEntryPoint authEntryPoint() {
		return new AuthEntryPoint("/login");
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
				.requestMatchers("/actionLogin**", "/login**", "/crypto/**", "/loginError**").permitAll()
				.requestMatchers("/properties/**").hasRole("ADMIN")
				.requestMatchers("/api/a/**").hasRole("A")
				.requestMatchers("/api/b/**").hasRole("B")
				.anyRequest().authenticated());
		
		http.addFilterAt(authenticationFilter(authenticationManager(http)),
		                 UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(jwtFilter, AuthenticationFilter.class);
		
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(authEntryPoint()));


//		http.logout(logout -> logout
//				.logoutUrl("/logout")
//				.permitAll()
//				.clearAuthentication(true)
//				.invalidateHttpSession(true)
//				.logoutSuccessHandler(null));
		
		http.sessionManagement(configurer -> configurer
				.sessionFixation()
				.changeSessionId()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler("/main");
	}
	
	@Bean
	public LoginFailureHandler loginFailureHandler() {
		return new LoginFailureHandler("/loginError");
	}
	
}
