package com.demo.spring.config.jdbc.mybatis;


import com.demo.spring.config.jdbc.mybatis.util.MybatisSortGenerator;
import com.demo.spring.config.jdbc.mybatis.util.PGSortGenerator;
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Setter
@Configuration
@MapperScan(basePackages = {"com.demo.spring.**.mapper"})
@ConfigurationProperties(prefix = "mybatis.configuration")
public class MybatisConfig {
	
	private boolean mapUnderscoreToCamelCase;
	
	private JdbcType jdbcTypeForNull;
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSession = new SqlSessionFactoryBean();
		sqlSession.setDataSource(dataSource);
		
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		sqlSession.setMapperLocations(pmrpr.getResources("classpath:/mapper/**/*.xml"));
		
		sqlSession.setTypeAliasesPackage("com.demo.spring.**");
		
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setJdbcTypeForNull(jdbcTypeForNull);
		configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
		sqlSession.setConfiguration(configuration);
		
		return sqlSession.getObject();
	}
	
	@Bean
	public MybatisSortGenerator mybatisSortGenerator() {
		return new PGSortGenerator();
	}
}
