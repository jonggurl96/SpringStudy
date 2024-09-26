package com.demo.spring.usr.service.mybatis;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.vo.User;

import java.util.List;

public interface UserMybatisService {
	
	public List<User> testSort(SearchDTO searchDTO);
	
}
