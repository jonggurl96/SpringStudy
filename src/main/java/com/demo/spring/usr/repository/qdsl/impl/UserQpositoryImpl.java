package com.demo.spring.usr.repository.qdsl.impl;


import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.qdsl.UserQpository;
import com.demo.spring.usr.vo.QUser;
import com.demo.spring.usr.vo.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserQpositoryImpl implements UserQpository {
	
	private final JPAQueryFactory qf;
	
	@Override
	public void increaseCntLoginFailr(UserDTO userDTO) {
		QUser qUser = QUser.user;
		
		qf.update(qUser)
		  .set(qUser.cntLoginFailr, userDTO.getCntLoginFailr() + 1)
		  .where(qUser.userNo.eq(userDTO.getUserNo()))
		  .execute();
	}
	
	@Override
	public void initCntLoginFailr(UserDTO userDTO) {
		QUser qUser = QUser.user;
		
		qf.update(qUser)
		  .set(qUser.cntLoginFailr, 0)
		  .where(qUser.userNo.eq(userDTO.getUserNo()))
		  .execute();
	}
	
	@Override
	public List<User> testSort(OrderSpecifier<?>[] orderSpecifiers) {
		QUser qUser = QUser.user;
		
		return qf.selectFrom(qUser)
		         .orderBy(orderSpecifiers)
		         .fetch();
	}
	
}
