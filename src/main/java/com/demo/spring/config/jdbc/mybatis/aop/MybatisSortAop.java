package com.demo.spring.config.jdbc.mybatis.aop;


import com.demo.spring.config.jdbc.mybatis.util.MybatisSortGenerator;
import com.demo.spring.config.jdbc.util.SearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MybatisSortAop {
	
	private final MybatisSortGenerator mybatisSortGenerator;
	
	@Before(
			value = "execution(public java.lang.Iterable+ com.*..*MybatisServiceImpl.*(..)) && args(searchDTO, ..)",
			argNames = "searchDTO")
	public void setOrderByClause(SearchDTO searchDTO) {
		String orderByClause = mybatisSortGenerator.generate(searchDTO.getSortDescriptions());
		searchDTO.setOrderByClause(orderByClause);
	}
}
