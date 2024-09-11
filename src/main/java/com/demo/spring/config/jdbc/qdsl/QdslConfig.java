package com.demo.spring.config.jdbc.qdsl;


import com.demo.spring.config.jdbc.qdsl.util.QdslSortGenerator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdslConfig {
	
	@PersistenceContext
	private EntityManager em;
	
	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}
	
	@Bean
	public QdslSortGenerator qdslSortGenerator() {
		return new QdslSortGenerator();
	}
}
