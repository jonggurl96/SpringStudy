package com.demo.spring.sec.role.usr.service.mybatis.impl;


import com.demo.spring.sec.role.usr.repository.mapper.UserAuthorMapper;
import com.demo.spring.sec.role.usr.service.mybatis.UserAuthorMybatisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorMybatisServiceImpl implements UserAuthorMybatisService {
	
	private final UserAuthorMapper userAuthorMapper;
	
	@Override
	public String getAuthorityString(String userNo) {
		return String.join(" > ", userAuthorMapper.getAuthorities(userNo));
	}
	
}
