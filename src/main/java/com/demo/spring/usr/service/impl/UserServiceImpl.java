package com.demo.spring.usr.service.impl;


import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.UserRepository;
import com.demo.spring.usr.service.UserService;
import com.demo.spring.usr.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	/**
	 * userId, pwd를 입력받아 로그인
	 *
	 * @param userDTO
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	@Override
	public void login(UserDTO userDTO) throws Exception {
		User user = userRepository.findByUserId(userDTO.getUserId());
//		if(user.getPassword().equals(encPwd(userDTO.getPwd())))
//			return new UserDTO(user);
//		throw new RuntimeException("Failed Log In.");
	}
	
	/**
	 * 사용자가 입력한 pwd를 암호화
	 *
	 * @param pwd
	 *
	 * @return
	 */
	private String encPwd(String pwd) {
		return pwd;
	}
	
}
