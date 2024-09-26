package com.demo.spring.config.jdbc.util;


import java.util.List;

public interface SortGenerator<T> {
	
	public T generate(List<SortDescription> descriptions);
	
	public default boolean available(SortDescription description) {
		return description.getProp() != null && !description.getProp().isBlank();
	}
	
}
