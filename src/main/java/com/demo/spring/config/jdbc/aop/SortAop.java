package com.demo.spring.config.jdbc.aop;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.config.jdbc.util.SortDescription;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SortAop {
	
	private final ClassAliasProvider classAliasProvider;
	
	@Before(
			value = "execution(public java.lang.Iterable+ com.*..*Repository.*(..) ) && args(searchDTO, ..)",
			argNames = "searchDTO")
	public void setClassProp(SearchDTO searchDTO) {
		searchDTO.getSortDescriptions().forEach(this::setClassProp);
	}
	
	public void setClassProp(SortDescription description) {
		description.setClazz(classAliasProvider.getAliasClass(description.getAlias()));
	}
	
}
