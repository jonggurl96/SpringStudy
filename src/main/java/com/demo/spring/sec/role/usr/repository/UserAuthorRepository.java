package com.demo.spring.sec.role.usr.repository;


import com.demo.spring.sec.role.usr.vo.UserAuthority;
import com.demo.spring.sec.role.usr.vo.UserAuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAuthorRepository extends JpaRepository<UserAuthority, UserAuthorityId> {
	
	/**
	 * {@snippet: SELECT * FROM tn_user_authority WHERE user_no = :userNo}
	 *
	 * @param userNo
	 *
	 * @return
	 */
	public List<UserAuthority> findByUserNo(String userNo);
	
}
