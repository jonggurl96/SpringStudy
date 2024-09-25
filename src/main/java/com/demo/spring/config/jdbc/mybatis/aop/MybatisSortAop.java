package com.demo.spring.config.jdbc.mybatis.aop;


import com.demo.spring.config.jdbc.mybatis.util.MybatisSortGenerator;
import com.demo.spring.config.jdbc.util.SearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MybatisSortAop {
	
	private final MybatisSortGenerator sortGenerator;
	
	@Pointcut(value = "bean(*Mapper)")
	private void mapperPointcut() {}
	
	@Before(value = "mapperPointcut()")
	public void set() {}
	
	@Before(
			value = "within(com.*..*Mapper) && args(searchDTO, ..)",
			argNames = "searchDTO")
	public void setOrderByClause(SearchDTO searchDTO) {
		String orderByClause = sortGenerator.generate(searchDTO.getSortDescriptions());
		searchDTO.setOrderByClause(orderByClause);
	}
}
