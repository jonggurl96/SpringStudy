package com.demo.spring.config.jdbc.util;


import com.demo.spring.config.jdbc.qdsl.annotation.ClassAlias;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * SortDescription.java
 * <pre>
 * 정렬 정의 객체
 *
 *  MyBatis    JPA   Query DSL
 * --------- ------ ------------
 *   prop     prop     alias
 *
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
@Slf4j
public class SortDescription {
	
	/**
	 * 정렬 우선순위
	 */
	private int priority;
	
	/**
	 * 정렬할 컬럼의 클래스 alias
	 * <p>
	 * Query DSL 사용 시 Path 찾기 위한 alias
	 * <p>
	 *
	 * @see ClassAlias
	 */
	private String alias;
	
	/**
	 * 정렬할 컬럼의 클래스 타입
	 */
	private Class<?> clazz;
	
	/**
	 * 정렬할 column name
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
	
	public String getAggr() {
		return aggr == null ? null : aggr.toUpperCase();
	}
	
}
