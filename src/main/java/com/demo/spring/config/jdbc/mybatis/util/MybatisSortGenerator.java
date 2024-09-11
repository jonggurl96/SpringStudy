package com.demo.spring.config.jdbc.mybatis.util;


import com.demo.spring.config.jdbc.util.SortDescription;

import java.util.List;

public interface MybatisSortGenerator {
	
	public String generate(List<SortDescription> descriptions);
	
}
