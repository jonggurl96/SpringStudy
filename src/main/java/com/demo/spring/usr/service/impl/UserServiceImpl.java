package com.demo.spring.usr.service.impl;


import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.UserRepository;
import com.demo.spring.usr.repository.jpa.UserJpository;
import com.demo.spring.usr.repository.qdsl.UserQpository;
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
	 * @param userId
	 *
	 * @see UserJpository#findByUserId(String)
	 */
	@Override
	public User findByUserId(String userId) {
		log.debug(">>> [JPA] Find Entity User By user_id {}", userId);
		return userRepository.findByUserId(userId);
	}
	
	/**
	 * @param userNo
	 *
	 * @see UserJpository#findByUserNo(String)
	 */
	@Override
	public User findByUserNo(String userNo) {
		log.debug(">>> [JPA] Find Entity User By user_no {}", userNo);
		return userRepository.findByUserNo(userNo);
	}
	
	/**
	 * @param userDTO
	 *
	 * @see UserQpository#increaseCntLoginFailr(UserDTO)
	 */
	@Override
	public void increaseCntLoginFailr(UserDTO userDTO) {
		int presentCnt = userDTO.getCntLoginFailr();
		log.debug(">>> [QDSL] User {} Increase Login Failure Count {} to {}",
		          userDTO.getUserNo(),
		          presentCnt,
		          presentCnt + 1);
		userRepository.increaseCntLoginFailr(userDTO);
	}
	
	/**
	 * @param userDTO
	 *
	 * @see UserQpository#initCntLoginFailr(UserDTO)
	 */
	@Override
	public void initCntLoginFailr(UserDTO userDTO) {
		log.debug(">>> [QDSL] User {} Init Login Failure Count", userDTO.getUserNo());
		userRepository.initCntLoginFailr(userDTO);
	}
	
}
