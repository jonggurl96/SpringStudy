package com.demo.spring.config.security.auth;


import com.demo.spring.usr.dto.UserDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
	
	private UserDTO userDTO;
	
	private List<GrantedAuthority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return userDTO.getPwd();
	}
	
	@Override
	public String getUsername() {
		return this.userDTO.getUserNo();
	}
	
	public int getLoginFailrCnt() {
		return userDTO.getCntLoginFailr();
	}
	
	public boolean isExceedLoginFailrCnt(int maxCnt) {
		return userDTO.getCntLoginFailr() >= maxCnt;
	}
	
}
