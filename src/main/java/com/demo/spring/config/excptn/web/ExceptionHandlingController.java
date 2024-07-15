package com.demo.spring.config.excptn.web;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionHandlingController implements ErrorController {
	
	@RequestMapping("/error")
	public void handleError(HttpServletRequest request) throws Exception {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if(status == null) return;
		
		int statusCode = Integer.parseInt(status.toString());
		
		throw switch(statusCode) {
			case 400 -> new RuntimeException("BadRequest");
			case 401 -> new RuntimeException("SessionRequired");
			case 403 -> new RuntimeException("Forbidden");
			case 404 -> new RuntimeException("NotFound");
			case 500 -> (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
			default -> new Exception("Other Exception");
		};
	}
	
}
