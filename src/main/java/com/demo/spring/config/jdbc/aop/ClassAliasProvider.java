package com.demo.spring.config.jdbc.aop;


import com.demo.spring.config.jdbc.annotation.ClassAlias;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ClassAliasProvider {
	
	private final ClassPathScanningCandidateComponentProvider provider;
	
	private final String BASE_PACKAGE;
	
	private final Map<String, Class<?>> classMap = new HashMap<>();
	
	private boolean initialized = false;
	
	public ClassAliasProvider() {
		provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(ClassAlias.class));
		BASE_PACKAGE = "com.demo.spring";
	}
	
	public Set<Class<?>> findAnnotationClasses() {
		Set<Class<?>> classes = new HashSet<>();
		for(BeanDefinition definition : provider.findCandidateComponents(BASE_PACKAGE)) {
			String beanClassName = definition.getBeanClassName();
			try {
				classes.add(Class.forName(beanClassName));
			} catch(ClassNotFoundException ignored) {
				log.error(">>> ClassNotFoundException. {}", beanClassName);
			}
		}
		return classes;
	}
	
	private void init() {
		Set<Class<?>> classes = findAnnotationClasses();
		classes.forEach(c -> {
			String alias = c.getAnnotation(ClassAlias.class).alias();
			classMap.put(alias, c);
		});
		initialized = true;
	}
	
	public Class<?> getAliasClass(String alias) {
		if(!initialized)
			init();
		return classMap.get(alias);
	}
	
}
