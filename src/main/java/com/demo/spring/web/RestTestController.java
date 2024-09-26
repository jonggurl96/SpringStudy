package com.demo.spring.web;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.service.jpa.UserJpaService;
import com.demo.spring.usr.service.mybatis.UserMybatisService;
import com.demo.spring.usr.service.qdsl.UserQdslService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test/rest")
@RequiredArgsConstructor
public class RestTestController {
	
	private final UserMybatisService userMybatisService;
	
	private final UserJpaService userJpaService;
	
	private final UserQdslService userQdslService;
	
	@PostMapping("/mybatis/sort")
	public List<?> mbsort(@RequestBody SearchDTO searchDTO) {
		log.debug(">>> /test/rest/mybatis/sort.");
		return userMybatisService.testSort(searchDTO);
	}
	
	@PostMapping("/jpa/sort")
	public List<?> jpasort(@RequestBody SearchDTO searchDTO) {
		log.debug(">>> /test/rest/jpa/sort.");
		return userJpaService.testSort(searchDTO);
	}
	
	@PostMapping("/qdsl/sort")
	public List<?> qdslsort(@RequestBody SearchDTO searchDTO) {
		log.debug(">>> /test/rest/qdsl/sort.");
		return userQdslService.testSort(searchDTO);
	}
	
}
