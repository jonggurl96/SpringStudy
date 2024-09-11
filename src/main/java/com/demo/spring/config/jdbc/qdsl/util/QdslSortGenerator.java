package com.demo.spring.config.jdbc.qdsl.util;


import com.demo.spring.config.jdbc.util.SortDescription;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.PathBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QdslSortGenerator {
	
	public List<OrderSpecifier<?>> generate(List<SortDescription> descriptions) throws NoSuchFieldException {
		return descriptions.stream()
		                   .sorted(Comparator.comparingInt(SortDescription::getPriority))
		                   .map(description -> {
			                   ComparableExpression<?> expression = createPath(description);
			                   OrderSpecifier<?> order = description.getDirection().equalsIgnoreCase("DESC")
			                                             ? expression.desc()
			                                             : expression.asc();
			                   return setNullHandling(order, description);
		                   }).collect(Collectors.toUnmodifiableList());
	}
	
	public OrderSpecifier<?> setNullHandling(OrderSpecifier<?> order, SortDescription description) {
		return switch(description.getNullHandling()) {
			case 1 -> order.nullsFirst();
			case 2 -> order.nullsLast();
			default -> order;
		};
	}
	
	public ComparablePath<?> createPath(SortDescription description) {
		Class<?> clazz = description.getClazz();
		PathBuilder<?> parent = new PathBuilder<>(clazz, clazz.getSimpleName());
		return parent.getComparable(description.getProp(), Comparable.class);
	}
	
}
