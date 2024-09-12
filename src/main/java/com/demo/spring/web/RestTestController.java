package com.demo.spring.web;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test/rest")
@RequiredArgsConstructor
public class RestTestController {
	
	private final UserRepository userRepository;
	
	@PostMapping("/mybatis/sort")
	public List<?> mbsort(SearchDTO searchDTO) {
		log.debug(">>> /test/rest/mybatis/sort.");
		return userRepository.testSortMb(searchDTO);
	}
	
	@PostMapping("/jpa/sort")
	public List<?> jpasort(SearchDTO searchDTO) {
		log.debug(">>> /test/rest/jpa/sort.");
		return userRepository.testSortJpa(searchDTO);
	}
	
	@PostMapping("/qdsl/sort")
	public List<?> qdslsort(SearchDTO searchDTO) {
		log.debug(">>> /test/rest/qdsl/sort.");
		return userRepository.testSortQdsl(searchDTO);
	}
}
