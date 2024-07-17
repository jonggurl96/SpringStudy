package com.demo.spring.config.security.service;


import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.sec.role.usr.repository.UserAuthorRepository;
import com.demo.spring.sec.role.usr.vo.UserAuthority;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.UserRepository;
import com.demo.spring.usr.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	private final UserAuthorRepository userAuthorRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(username);
		if(user == null)
			throw new UsernameNotFoundException("User Name " + username + "Not found Exception occured.");
		UserDTO userDTO = new UserDTO(user);
		List<UserAuthority> userAuthorities = userAuthorRepository.findByUserNo(userDTO.getUserNo());
		return CustomUserDetails.builder()
				.userDTO(userDTO)
				.authorities(userAuthorities.stream()
						             .map(UserAuthority::getAuthorCode)
						             .map(SimpleGrantedAuthority::new)
						             .collect(Collectors.toUnmodifiableList()))
				.build();
	}
	
}
