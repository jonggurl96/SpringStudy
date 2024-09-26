package com.demo.spring.usr.service.mybatis.impl;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.repository.mapper.UserMapper;
import com.demo.spring.usr.service.mybatis.UserMybatisService;
import com.demo.spring.usr.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMybatisServiceImpl implements UserMybatisService {
	
	private final UserMapper userMapper;
	
	@Override
	public List<User> testSort(SearchDTO searchDTO) {
		return userMapper.testSort(searchDTO);
	}
	
}
