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

/**
 * ClassAliasProvider.java
 * <pre>
 * ClassAlias annotation을 명시한 클래스를 찾아 alias와 QClass FQN을 매칭
 * 빈 등록 후 최초 호출 시점 1회 스캔 후 저장
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @see com.demo.spring.config.jdbc.annotation.ClassAlias
 * @since 2024-09-19
 */
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
		for(BeanDefinition definition: provider.findCandidateComponents(BASE_PACKAGE)) {
			String beanClassName = definition.getBeanClassName();
			
			if(beanClassName == null)
				continue;
			
			try {
				classes.add(Class.forName(beanClassName));
			} catch(ClassNotFoundException ignored) {
				log.error(">>> ClassNotFoundException. {}", beanClassName);
			}
		}
		return classes;
	}
	
	private void init() {
		log.debug(">>> Init Annotation Class Map.");
		Set<Class<?>> classes = findAnnotationClasses();
		classes.forEach(c -> {
			String packageName = c.getPackageName();
			String simpleName = c.getSimpleName();
			String qClassFqn = packageName + ".Q" + simpleName;
			
			String alias = c.getAnnotation(ClassAlias.class).value();
			log.debug(">>> {} class alias is {}.", c, alias);
			
			try {
				classMap.put(alias, Class.forName(qClassFqn));
			} catch(ClassNotFoundException ignored) {
				log.error(">>> Class {} Not Found.", qClassFqn);
			}
		});
		initialized = true;
	}
	
	public Class<?> getAliasClass(String alias) {
		if(!initialized)
			init();
		return classMap.get(alias);
	}
	
}
