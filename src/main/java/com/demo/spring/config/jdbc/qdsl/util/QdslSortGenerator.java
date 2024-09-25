package com.demo.spring.config.jdbc.qdsl.util;


import com.demo.spring.config.jdbc.qdsl.annotation.ClassAlias;
import com.demo.spring.config.jdbc.util.SortDescription;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * QdslSortGenerator.java
 * <pre>
 * 정렬 가능한 클래스
 *  1. @ClassAlias annotation을 추가한 클래스
 *
 * 정렬 가능한 property 속성
 *  1. Number: Integer, Double, Byte, Float, Short, Long, BigDecimal, Short, BigInteger
 *  2. Boolean
 *  3. String
 *  4. LocalDate
 *  5. LocalDateTime
 *  6. OffsetDateTime
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @see ClassAlias
 * @since 2024-09-19
 */

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
		
		if(expression == null)
			return null;
		
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
		
		if(clazz == null)
			return null;
		
		SimplePath<?> parent = Expressions.path(clazz, description.getAlias());
		
		Class<?> propType = getPropType(description);
		
		if(Number.class.isAssignableFrom(propType)) {
			try {
				return createAggrPath(description, propType);
			} catch(Exception e) {
				log.error(e.getMessage());
				return null;
			}
		}
		else if(Boolean.class.isAssignableFrom(propType))
			return Expressions.booleanPath(parent, description.getProp());
		else if(LocalDateTime.class.isAssignableFrom(propType))
			return Expressions.dateTimePath(LocalDateTime.class, parent, description.getProp());
		else if(OffsetDateTime.class.isAssignableFrom(propType))
			return Expressions.dateTimePath(OffsetDateTime.class, parent, description.getProp());
		else if(LocalDate.class.isAssignableFrom(propType))
			return Expressions.datePath(LocalDate.class, parent, description.getProp());
		return Expressions.stringPath(parent, description.getProp());
	}
	
	public ComparableExpressionBase<?> createAggrPath(SortDescription description, Class<?> propType) throws Exception {
		Class<?> clazz = description.getClazz();
		PathBuilder<?> parent = new PathBuilder<>(clazz, clazz.getSimpleName());
		NumberPath<?> path;
		
		if(Integer.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Integer.class, parent, description.getProp());
		else if(Double.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Double.class, parent, description.getProp());
		else if(Byte.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Byte.class, parent, description.getProp());
		else if(Float.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Float.class, parent, description.getProp());
		else if(Short.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Short.class, parent, description.getProp());
		else if(Long.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Long.class, parent, description.getProp());
		else if(BigDecimal.class.isAssignableFrom(propType))
			path = Expressions.numberPath(BigDecimal.class, parent, description.getProp());
		else if(Short.class.isAssignableFrom(propType))
			path = Expressions.numberPath(Short.class, parent, description.getProp());
		else if(BigInteger.class.isAssignableFrom(propType))
			path = Expressions.numberPath(BigInteger.class, parent, description.getProp());
		else throw new Exception("Not Supported Class. " + propType.getName());
		
		return aggregate(path, description.getAggr());
	}
	
	private ComparableExpressionBase<?> aggregate(NumberPath<?> path, String aggr) {
		return switch(aggr) {
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
	
	public Class<?> getPropType(SortDescription description) throws NoSuchFieldException {
		Field field = description.getClazz().getDeclaredField(description.getProp());
		field.setAccessible(true);
		return field.getType();
	}
	
}
