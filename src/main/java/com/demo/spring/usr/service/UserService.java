package com.demo.spring.usr.service;


import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.vo.User;

public interface UserService {
	
	/**
	 * @see com.demo.spring.usr.repository.jpa.UserJpository#findByUserId(String)
	 */
	public User findByUserId(String userId);
	
	/**
	 * @see com.demo.spring.usr.repository.jpa.UserJpository#findByUserNo(String)
	 */
	public User findByUserNo(String userNo);
	
	/**
	 * @see com.demo.spring.usr.repository.qdsl.UserQpository#increaseCntLoginFailr(UserDTO)
	 */
	public void increaseCntLoginFailr(UserDTO userDTO);
	
	/**
	 * @see com.demo.spring.usr.repository.qdsl.UserQpository#initCntLoginFailr(UserDTO)
	 */
	public void initCntLoginFailr(UserDTO userDTO);
	
}
