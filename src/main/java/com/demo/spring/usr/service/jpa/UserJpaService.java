package com.demo.spring.usr.service.jpa;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.vo.User;

import java.util.List;

public interface UserJpaService {
	
	public User findByUserId(String userId);
	
	public User findByUserNo(String userNo);
	
	public List<User> testSort(SearchDTO searchDTO);
	
}
