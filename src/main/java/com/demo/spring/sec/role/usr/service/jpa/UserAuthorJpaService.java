package com.demo.spring.sec.role.usr.service.jpa;


import com.demo.spring.sec.role.usr.vo.UserAuthority;

import java.util.List;

public interface UserAuthorJpaService {
	
	public List<UserAuthority> getAuthorities(String userNo);
	
}
