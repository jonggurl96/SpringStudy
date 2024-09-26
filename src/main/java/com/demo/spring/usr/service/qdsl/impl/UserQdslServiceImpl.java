package com.demo.spring.usr.service.qdsl.impl;


import com.demo.spring.config.jdbc.qdsl.util.QdslSortGenerator;
import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.repository.qdsl.UserQpository;
import com.demo.spring.usr.service.qdsl.UserQdslService;
import com.demo.spring.usr.vo.User;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQdslServiceImpl implements UserQdslService {
	
	private final UserQpository userQpository;
	
	private final QdslSortGenerator qdslSortGenerator;
	
	@Override
	public void increaseCntLoginFailr(UserDTO userDTO) {
		int presentCnt = userDTO.getCntLoginFailr();
		log.debug(">>> [QDSL] User {} Increase Login Failure Count {} to {}",
		          userDTO.getUserNo(),
		          presentCnt,
		          presentCnt + 1);
		userQpository.increaseCntLoginFailr(userDTO);
	}
	
	@Override
	public void initCntLoginFailr(UserDTO userDTO) {
		log.debug(">>> [QDSL] User {} Init Login Failure Count", userDTO.getUserNo());
		userQpository.initCntLoginFailr(userDTO);
	}
	
	@Override
	public List<User> testSort(SearchDTO searchDTO) {
		return userQpository.testSort(getOrderByClause(searchDTO));
	}
	
	private OrderSpecifier<?>[] getOrderByClause(SearchDTO searchDTO) {
		return qdslSortGenerator.generate(searchDTO.getSortDescriptions());
	}
	
}
