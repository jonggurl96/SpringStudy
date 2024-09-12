package com.demo.spring.config.jdbc.jpa.util;


import com.demo.spring.config.jdbc.util.SortDescription;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

public class JpaSortGenerator {
	
	public Sort generate(List<SortDescription> descriptions) {
		List<Sort.Order> orders = descriptions.stream()
		                                      .sorted(Comparator.comparingInt(SortDescription::getPriority))
		                                      .map(this::getOrder)
		                                      .toList();
		return Sort.by(orders);
	}
	
	public Sort.Order getOrder(SortDescription description) {
		Sort.Direction direction = Sort.Direction.fromString(description.getDirection());
		
		Sort.NullHandling nullHandling = getNullHandling(description);
		
		return new Sort.Order(direction, description.getProp(), nullHandling);
	}
	
	public Sort.NullHandling getNullHandling(SortDescription description) {
		return switch(description.getNullHandling()) {
			case 1 -> Sort.NullHandling.NULLS_FIRST;
			case 2 -> Sort.NullHandling.NULLS_LAST;
			default -> Sort.NullHandling.NATIVE;
		};
	}
	
}
