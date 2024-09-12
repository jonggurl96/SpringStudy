package com.demo.spring.usr.repository;


import com.demo.spring.config.jdbc.jpa.util.JpaSortGenerator;
import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.jpa.UserJpository;
import com.demo.spring.usr.repository.mapper.UserMapper;
import com.demo.spring.usr.repository.qdsl.UserQpository;
import com.demo.spring.usr.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserRepository.java
 * <pre>
 * User Entity Repository Structure
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 9. 6.
 */
@Component
@RequiredArgsConstructor
public class UserRepository {
	
	/**
	 * MyBatis Repository
	 */
	private final UserMapper userMapper;
	
	/**
	 * JPA Repository
	 */
	private final UserJpository userJpository;
	
	/**
	 * Query DSL Repository
	 */
	private final UserQpository userQpository;
	
	private final JpaSortGenerator jpaSortGenerator;
	
	public User findByUserId(String userId) {
		return userJpository.findByUserId(userId);
	}
	
	public User findByUserNo(String userNo) {
		return userJpository.findByUserNo(userNo);
	}
	
	public void increaseCntLoginFailr(UserDTO userDTO) {
		userQpository.increaseCntLoginFailr(userDTO);
	}
	
	public void initCntLoginFailr(UserDTO userDTO) {
		userQpository.initCntLoginFailr(userDTO);
	}
	
	public List<User> testSortMb(SearchDTO searchDTO) {
		return userMapper.testSort(searchDTO);
	}
	
	public List<User> testSortJpa(SearchDTO searchDTO) {
		Sort sort = jpaSortGenerator.generate(searchDTO.getSortDescriptions());
		return userJpository.findAll(sort);
	}
	
	public List<User> testSortQdsl(SearchDTO searchDTO) {
		return userQpository.testSort(searchDTO);
	}
}
