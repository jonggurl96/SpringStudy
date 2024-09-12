package com.demo.spring.config.jdbc.util;


import lombok.*;

/**
 * SortDescription.java
 * <pre>
 * 정렬 정의 객체
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 9. 11.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SortDescription {
	
	/**
	 * 정렬 우선순위
	 */
	private int priority;
	
	/**
	 * 정렬할 컬럼의 클래스 alias
	 * <p>
	 * ${ @ClassAlias("alias").property }
	 */
	@Setter(AccessLevel.NONE)
	private String alias;
	
	/**
	 * 정렬할 컬럼의 클래스 타입
	 */
	private Class<?> clazz;
	
	/**
	 * 정렬할 컬럼의 alias 또는 column name
	 */
	private String prop;
	
	/**
	 * 정렬 방향, default "ASC"
	 */
	private String direction = "ASC";
	
	/**
	 * Null Handling Strategy
	 * <pre>
	 * - 0(default): NATIVE
	 * - 1         : NULLS_FIRST
	 * - 2         : NULLS_LAST
	 * </pre>
	 */
	private int nullHandling;
	
	@Getter(AccessLevel.NONE)
	private String aggr;
	
	public void setAlias(String alias) {
		this.alias = alias;
		
		String[] cp = alias.split("\\|");
		setProp(cp[1]);
	}
	
	public String getAggr() {
		return aggr== null ? null : aggr.toUpperCase();
	}
	
}
