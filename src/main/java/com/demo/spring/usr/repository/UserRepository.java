package com.demo.spring.usr.repository;


import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.jpa.UserJpository;
import com.demo.spring.usr.repository.qdsl.UserQpository;
import com.demo.spring.usr.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
	 * JPA Repository
	 */
	private final UserJpository userJpository;
	
	/**
	 * Query DSL Repository
	 */
	private final UserQpository userQpository;
	
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
	
}
