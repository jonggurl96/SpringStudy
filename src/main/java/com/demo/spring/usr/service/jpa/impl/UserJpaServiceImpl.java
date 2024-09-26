package com.demo.spring.usr.service.jpa.impl;


import com.demo.spring.config.jdbc.jpa.util.JpaSortGenerator;
import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.repository.jpa.UserJpository;
import com.demo.spring.usr.service.jpa.UserJpaService;
import com.demo.spring.usr.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserJpaServiceImpl implements UserJpaService {
	
	private final UserJpository userJpository;
	
	private final JpaSortGenerator jpaSortGenerator;
	
	@Override
	public User findByUserId(String userId) {
		log.debug(">>> [JPA] Find Entity User By user_id {}", userId);
		return userJpository.findByUserId(userId);
	}
	
	@Override
	public User findByUserNo(String userNo) {
		log.debug(">>> [JPA] Find Entity User By user_no {}", userNo);
		return userJpository.findByUserNo(userNo);
	}
	
	@Override
	public List<User> testSort(SearchDTO searchDTO) {
		Sort sort = jpaSortGenerator.generate(searchDTO.getSortDescriptions());
		return userJpository.findAll(sort);
	}
	
}
