package com.demo.spring.config.jdbc.mybatis.util;


import com.demo.spring.config.jdbc.util.SortDescription;

import java.util.Comparator;
import java.util.List;

public class PGSortGenerator implements MybatisSortGenerator {
	
	@Override
	public String generate(List<SortDescription> descriptions) {
		return String.join(", ",
		                   descriptions.stream()
		                               .filter(this::available)
		                               .sorted(Comparator.comparingInt(SortDescription::getPriority))
		                               .map(this::nullHandling).toList());
	}
	
	public String nullHandling(SortDescription description) {
		String prop = aggr(description);
		String ret = switch(description.getNullHandling()) {
			case 1 -> prop + " ISNULL DESC, ";
			case 2 -> prop + " ISNULL, ";
			default -> "";
		};
		return ret + prop + " " + getDirection(description);
	}
	
	public String aggr(SortDescription description) {
		List<String> aggregates = List.of("ABS", "AVG", "CEIL", "FLOOR", "NEGATE", "ROUND", "SQRT", "SUM");
		String aggrFnct = description.getAggr();
		String prop = description.getProp();
		
		if(aggrFnct == null || !aggregates.contains(aggrFnct))
			return prop;
		
		return (aggrFnct.equalsIgnoreCase("NEGATE") ? "-(" : (aggrFnct + "(")) + prop + ")";
	}
	
	private String getDirection(SortDescription description) {
		String direction = description.getDirection();
		return direction == null || direction.isBlank() ? "ASC" : direction;
	}
	
}
