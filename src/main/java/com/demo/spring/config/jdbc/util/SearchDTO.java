package com.demo.spring.config.jdbc.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Alias("searchdto")
public class SearchDTO {
	
	private List<SortDescription> sortDescriptions;
	
	private String orderByClause;
	
}
