package com.demo.spring.sec.role.usr.repository;


import com.demo.spring.sec.role.usr.repository.mapper.UserAuthorMapper;
import com.demo.spring.sec.role.usr.vo.UserAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAuthorRepository {
	
	private final UserAuthorJpository userAuthorJpository;
	
	private final UserAuthorMapper userAuthorMapper;
	
	public List<UserAuthority> getAuthorities(String userNo) {
		return userAuthorJpository.findByUserNo(userNo);
	}
	
	public String getAuthorityString(String userNo) {
		return String.join(" > ", userAuthorMapper.getAuthorities(userNo));
	}
}
