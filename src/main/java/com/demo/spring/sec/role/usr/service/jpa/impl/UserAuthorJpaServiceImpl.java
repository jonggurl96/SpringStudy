package com.demo.spring.sec.role.usr.service.jpa.impl;


import com.demo.spring.sec.role.usr.repository.jpa.UserAuthorJpository;
import com.demo.spring.sec.role.usr.service.jpa.UserAuthorJpaService;
import com.demo.spring.sec.role.usr.vo.UserAuthority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorJpaServiceImpl implements UserAuthorJpaService {
	
	private final UserAuthorJpository userAuthorJpository;
	
	@Override
	public List<UserAuthority> getAuthorities(String userNo) {
		return userAuthorJpository.findByUserNo(userNo);
	}
	
}
