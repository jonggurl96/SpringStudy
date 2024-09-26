package com.demo.spring.config.jdbc.mybatis.util;


import com.demo.spring.config.jdbc.util.SortDescription;
import com.demo.spring.config.jdbc.util.SortGenerator;

import java.util.List;

public interface MybatisSortGenerator extends SortGenerator<String> {
	
	public String generate(List<SortDescription> descriptions);
	
}
