package com.demo.spring.usr.repository.mapper;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
	
	public List<User> testSort(SearchDTO searchDTO);
	
}
