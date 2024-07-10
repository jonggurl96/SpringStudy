package com.demo.spring.config.excptn.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * GlobalExceptionHandler.java
 * <pre>
 * Error Page Exception Handler
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 10.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	
	private final String DEFAULT_ERROR_PAGE = "/err/default";
	
	private final String DEFAULT_ERROR_MESSAGE = "알 수 없는 오류가 발생했습니다. 관리자에게 문의해주세요.";
	
	@ExceptionHandler({
			RuntimeException.class,
			Exception.class
	})
	public ModelAndView doResolveException(Exception ex,
	                                       HttpServletRequest request,
	                                       HttpServletResponse response) {
		return new ModelAndView(DEFAULT_ERROR_PAGE);
	}
	
	private ModelAndView handleException(String view) {
		return new ModelAndView(view);
	}
	
	private boolean isAjax(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}
	
}
