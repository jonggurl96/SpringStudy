package com.demo.spring.web;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AbstractController {
	
	@ModelAttribute("contextPath")
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
}
