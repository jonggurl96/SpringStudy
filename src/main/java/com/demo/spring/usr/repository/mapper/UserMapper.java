package com.demo.spring.usr.repository.mapper;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.vo.User;

import java.util.List;

public interface UserMapper {
	
	public List<User> testSort(SearchDTO searchDTO);
	
}
