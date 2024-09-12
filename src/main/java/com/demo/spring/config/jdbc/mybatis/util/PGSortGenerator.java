package com.demo.spring.config.jdbc.mybatis.util;


import com.demo.spring.config.jdbc.util.SortDescription;

import java.util.Comparator;
import java.util.List;

public class PGSortGenerator implements MybatisSortGenerator {
	
	@Override
	public String generate(List<SortDescription> descriptions) {
		return String.join(", ",
		                   descriptions.stream()
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
		return ret + prop + " " + description.getDirection();
	}
	
	public String aggr(SortDescription description) {
		List<String> aggregates = List.of("ABS", "AVG", "CEIL", "FLOOR", "NEGATE", "ROUND", "SQRT", "SUM");
		String aggrFnct = description.getAggr();
		String prop = description.getProp();
		
		if(aggregates.contains(aggrFnct)) {
			if(aggrFnct.equalsIgnoreCase("NEGATE"))
				return "-(" + prop + ")";
			return aggrFnct + "(" + prop + ")";
		}
		
		return prop;
	}
}
