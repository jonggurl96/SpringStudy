package com.demo.spring.usr.service;


import com.demo.spring.usr.dto.UserDTO;

public interface UserService {
	
	/**
	 * userId, pwd를 입력받아 로그인
	 *
	 * @param userDTO
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public void login(UserDTO userDTO) throws Exception;
	
}
