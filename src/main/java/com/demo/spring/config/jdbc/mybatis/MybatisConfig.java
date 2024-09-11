package com.demo.spring.config.jdbc.mybatis;


import com.demo.spring.config.jdbc.mybatis.util.MybatisSortGenerator;
import com.demo.spring.config.jdbc.mybatis.util.PGSortGenerator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.demo.spring.**.mapper"})
public class MybatisConfig {
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSession = new SqlSessionFactoryBean();
		sqlSession.setDataSource(dataSource);
		
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		sqlSession.setMapperLocations(pmrpr.getResources("classpath:/mapper/**/*.xml"));
		
		return sqlSession.getObject();
	}
	
	@Bean
	public MybatisSortGenerator mybatisSortGenerator() {
		return new PGSortGenerator();
	}
}
