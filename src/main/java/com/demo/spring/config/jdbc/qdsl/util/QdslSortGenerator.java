package com.demo.spring.config.jdbc.qdsl.util;


import com.demo.spring.config.jdbc.util.SortDescription;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class QdslSortGenerator {
	
	public List<OrderSpecifier<?>> generate(List<SortDescription> descriptions) {
		return descriptions.stream()
		                   .sorted(Comparator.comparingInt(SortDescription::getPriority))
		                   .map(this::toOrderSpec)
		                   .filter(Objects::nonNull)
		                   .collect(Collectors.toUnmodifiableList());
	}
	
	public OrderSpecifier<?> toOrderSpec(SortDescription description) {
		ComparableExpressionBase<?> expression;
		try {
			expression = createPath(description);
		} catch(NoSuchFieldException ignored) {
			log.error(">>> Class {} Has No Field {}.", description.getClazz().getName(), description.getProp());
			return null;
		}
		OrderSpecifier<?> order = description.getDirection().equalsIgnoreCase("DESC")
		                          ? expression.desc()
		                          : expression.asc();
		return setNullHandling(order, description);
	}
	
	public OrderSpecifier<?> setNullHandling(OrderSpecifier<?> order, SortDescription description) {
		return switch(description.getNullHandling()) {
			case 1 -> order.nullsFirst();
			case 2 -> order.nullsLast();
			default -> order;
		};
	}
	
	public ComparableExpressionBase<?> createPath(SortDescription description) throws NoSuchFieldException {
		Class<?> clazz = description.getClazz();
		PathBuilder<?> parent = new PathBuilder<>(clazz, clazz.getSimpleName());
		
		Class<?> propType = getPropType(description);
		if(isNumber(propType)) {
			try {
				return createAggrPath(description, propType);
			} catch(Exception e) {
				log.error(e.getMessage());
			}
		}
		return parent.getComparable(description.getProp(), Comparable.class);
	}
	
	public ComparableExpressionBase<?> createAggrPath(SortDescription description, Class<?> propType) throws Exception {
		Class<?> clazz = description.getClazz();
		PathBuilder<?> parent = new PathBuilder<>(clazz, clazz.getSimpleName());
		NumberPath<?> path;
		
		if(Integer.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Integer.class);
		else if(Double.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Double.class);
		else if(Byte.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Byte.class);
		else if(Float.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Float.class);
		else if(Short.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Short.class);
		else if(Long.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Long.class);
		else if(BigDecimal.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), BigDecimal.class);
		else if(Short.class.isAssignableFrom(propType))
			path = parent.getNumber(description.getProp(), Short.class);
		else throw new Exception("Not Supported Class. " + propType.getName());
		
		return switch(description.getAggr()) {
			case "ABS" -> path.abs();
			case "AVG" -> path.avg();
			case "CEIL" -> path.ceil();
			case "FLOOR" -> path.floor();
			case "NEGATE" -> path.negate();
			case "ROUND" -> path.round();
			case "SQRT" -> path.sqrt();
			case "SUM" -> path.sum();
			default -> path;
		};
	}
	
	public boolean isNumber(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}
	
	public Class<?> getPropType(SortDescription description) throws NoSuchFieldException {
		Field field = description.getClazz().getDeclaredField(description.getProp());
		field.setAccessible(true);
		return field.getType();
	}
	
}
