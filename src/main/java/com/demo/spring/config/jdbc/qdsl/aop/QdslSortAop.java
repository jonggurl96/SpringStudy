package com.demo.spring.config.jdbc.qdsl.aop;


import com.demo.spring.config.jdbc.qdsl.annotation.provider.ClassAliasProvider;
import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.config.jdbc.util.SortDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QdslSortAop {
	
	private final ClassAliasProvider classAliasProvider;
	
	@Before(
			value = "execution(public java.lang.Iterable+ com.*..*QdslServiceImpl.*(..) ) && args(searchDTO, ..)",
			argNames = "searchDTO")
	public void setClassProp(SearchDTO searchDTO) {
		searchDTO.getSortDescriptions().forEach(this::setClassProp);
	}
	
	public void setClassProp(SortDescription description) {
		description.setClazz(classAliasProvider.getAliasClass(description.getAlias()));
	}
	
}
