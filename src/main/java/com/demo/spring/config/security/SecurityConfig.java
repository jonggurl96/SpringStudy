package com.demo.spring.config.security;


import com.demo.spring.config.security.filter.AuthenticationFilter;
import com.demo.spring.config.security.handler.AuthEntryPoint;
import com.demo.spring.config.security.handler.CustomAcessDeniedHandler;
import com.demo.spring.config.security.handler.LoginFailureHandler;
import com.demo.spring.config.security.handler.LoginSuccessHandler;
import com.demo.spring.config.security.provider.JpaDaoAuthProvider;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import jakarta.servlet.DispatcherType;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	
	private final RsaAesProperties rsaAesProperties;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JpaDaoAuthProvider provider() {
		return new JpaDaoAuthProvider(userDetailsService, passwordEncoder());
	}
	
	public ProviderManager providerManager() {
		ProviderManager manager = new ProviderManager(List.of(provider()));
		manager.setEraseCredentialsAfterAuthentication(false);
		return manager;
	}
	
	public AuthenticationFilter authenticationFilter() throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter(rsaAesProperties);
		filter.setFilterProcessesUrl("/actionLogin");
		filter.setAuthenticationManager(providerManager());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler("/main"));
		filter.setAuthenticationFailureHandler(new LoginFailureHandler("/loginError"));
		filter.setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());
		filter.setAllowSessionCreation(false);
		return filter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// CSRF 방지
		http.csrf(AbstractHttpConfigurer::disable);
		
		// 기본적인 http 로그인 방식 미사용
		http.httpBasic(AbstractHttpConfigurer::disable);
		
		// 커스텀 필터인 AuthenticationFilter가 폼 로그인을 대체하므로 비활성화
		http.formLogin(AbstractHttpConfigurer::disable);
		
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers("/crypto/**", "/loginError**", "/login", "/actionLogin").permitAll()
				.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
				.requestMatchers("/properties/**").hasRole("ADMIN")
				.requestMatchers("/api/a/**").hasRole("A")
				.requestMatchers("/api/b/**").hasRole("B")
				.dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()
				.anyRequest().authenticated());
		
		http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.addFilterBefore(new SecurityContextHolderFilter(new HttpSessionSecurityContextRepository()),
		                     AuthenticationFilter.class);
		
		http.securityContext(context -> context.securityContextRepository(
				                                       new DelegatingSecurityContextRepository(new HttpSessionSecurityContextRepository(),
				                                                                               new RequestAttributeSecurityContextRepository()))
		                                       .requireExplicitSave(true));
		
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthEntryPoint("/login"))
		                                         .accessDeniedHandler(new CustomAcessDeniedHandler()));

//		http.logout(logout -> logout
//				.logoutUrl("/logout")
//				.permitAll()
//				.clearAuthentication(true)
//				.invalidateHttpSession(true)
//				.logoutSuccessHandler(new LogoutSuccessHandler("/login")));
		
		http.sessionManagement(configurer -> configurer
				.sessionFixation()
				.changeSessionId()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.maximumSessions(1)
				.maxSessionsPreventsLogin(true));
		
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);
		
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
}
