package com.demo.spring.usr.repository.jpa;


import com.demo.spring.usr.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpository extends JpaRepository<User, String> {
	
	/**
	 * {@snippet: SELECT * FROM tn_users WHERE user_id = :userId}
	 *
	 * @param userId
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public User findByUserId(String userId);
	
	/**
	 * {@snippet: SELECT * FROM tn_users WHERE user_no = :userNo}
	 *
	 * @param userNo
	 *
	 * @return
	 */
	public User findByUserNo(String userNo);
	
}
