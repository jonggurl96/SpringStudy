package com.demo.spring.sec.role.usr.repository.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAuthorMapper {
	
	public List<String> getAuthorities(@Param("userNo") String userNo);
	
}
