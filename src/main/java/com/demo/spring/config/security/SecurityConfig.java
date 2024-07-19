package com.demo.spring.config.security;


import com.demo.spring.config.security.filter.AuthenticationFilter;
import com.demo.spring.config.security.handler.AuthEntryPoint;
import com.demo.spring.config.security.handler.LoginFailureHandler;
import com.demo.spring.config.security.handler.LoginSuccessHandler;
import com.demo.spring.config.security.provider.JpaDaoAuthProvider;
import com.demo.spring.config.security.util.helper.DecoderGenHelper;
import com.demo.spring.config.security.util.vo.DecoderVO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	
	private final DecoderGenHelper<? extends DecoderVO> genHelper;
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JpaDaoAuthProvider jpaDaoAuthProvider() {
		return new JpaDaoAuthProvider(userDetailsService, bCryptPasswordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(jpaDaoAuthProvider())
				.build();
	}
	
	@Bean
	public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter(genHelper);
		filter.setFilterProcessesUrl("/actionLogin");
		filter.setAuthenticationManager(authenticationManager);
		filter.setAuthenticationSuccessHandler(loginSuccessHandler());
		filter.setAuthenticationFailureHandler(loginFailureHandler());
		return filter;
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		
		http.formLogin(page -> page.loginPage("/login").permitAll());
		
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers("/actionLogin", "/login**")
				.permitAll()
				.requestMatchers("/api/a/**").hasRole("A")
				.requestMatchers("/api/b/**").hasRole("B")
				.anyRequest()
				.authenticated());

		http.addFilterBefore(authenticationFilter(authenticationManager(http)), UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthEntryPoint("/login")));


//		http.logout(logout -> logout
//				.logoutUrl("/logout")
//				.permitAll()
//				.clearAuthentication(true)
//				.invalidateHttpSession(true)
//				.logoutSuccessHandler(null));
		
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
	
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler("/main");
	}
	
	@Bean
	public LoginFailureHandler loginFailureHandler() {
		LoginFailureHandler loginFailureHandler = new LoginFailureHandler();
		loginFailureHandler.setForwardUrl("/login");
		return loginFailureHandler;
	}
	
}
