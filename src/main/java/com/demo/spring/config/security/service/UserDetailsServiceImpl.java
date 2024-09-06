package com.demo.spring.config.security.service;


import com.demo.spring.config.security.auth.CustomUserDetails;
import com.demo.spring.sec.role.usr.repository.UserAuthorRepository;
import com.demo.spring.sec.role.usr.vo.UserAuthority;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.service.UserService;
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
	
	private final UserService userService;
	
	private final UserAuthorRepository userAuthorRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUserId(username);
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
	
	public void increaseLoginFailrCnt(UserDTO userDTO) {
		userService.increaseCntLoginFailr(userDTO);
	}
	
	public void initLoginFailrCnt(UserDTO userDTO) {
		userService.initCntLoginFailr(userDTO);
	}
	
}
