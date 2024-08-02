package com.demo.spring.config.security.handler;


import com.demo.spring.config.security.exception.AccessDeniedException;
import com.demo.spring.config.security.exception.LoginFailedTooMuchException;
import com.demo.spring.config.security.exception.NotConfirmedException;
import com.demo.spring.config.security.exception.PasswordNotMatchException;
import com.demo.spring.config.security.exception.dec.DecryptException;
import com.demo.spring.config.security.util.LoginResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Setter
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private String forwardUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException exception) throws IOException, ServletException {
		String username = request.getParameter("username");
		log.error(">>> Username [{}] Login Failed.", username);
		
		switch(exception) {
			case InternalAuthenticationServiceException isae -> {
				switch(isae.getCause()) {
					case UsernameNotFoundException ignored ->
							setForUsernameNotFound(request, "입력한 아이디나 비밀번호에 적합한 사용자가 존재하지 않습니다.");
					case LoginFailedTooMuchException ignored ->
							setForLoginFailCoExceed(request, "로그인 시도 횟수를 초과한 계정입니다.");
					case NotConfirmedException ignored -> setForNotConfirmed(request, "인증이 완료되지 않은 계정입니다.");
					case AccountExpiredException ignored -> setForExpiredAccount(request, "탈퇴한 사용자입니다.");
					default -> setForUnknown(request, "알 수 없는 오류가 발생했습니다.");
				}
			}
			case PasswordNotMatchException pnme -> {
				String exMsg = pnme.getMessage();
				String[] nowMax = exMsg.split("/");
				String alertMsg = String.format("로그인 시도가 %s회 초과되었습니다.\n%s회 초과 시 본인인증을 거쳐야만 로그인 가능합니다.", nowMax[0], nowMax[1]);
				setForPwdNotMatch(request, alertMsg);
			}
			case BadCredentialsException ignored -> setForUsernameNotFound(request, "사용자 정보가 정확하지 않습니다.");
			case UsernameNotFoundException ignored -> setForUsernameNotFound(request, "사용자 정보가 존재하지 않습니다.");
			case AccountExpiredException ignored -> setForExpiredAccount(request, "탈퇴한 사용자입니다.");
			case LoginFailedTooMuchException ignored -> setForLoginFailCoExceed(request, "로그인 시도 횟수를 초과한 계정입니다.");
			case NotConfirmedException ignored -> setForNotConfirmed(request, "인증이 완료되지 않은 계정입니다.");
			case AccessDeniedException ignored -> setForAccessDenied(request, "접근이 거부되었습니다.");
			case DecryptException de -> {
				String encodedPassword = de.getMessage();
				setForPwdNotMatch(request, "패스워드 복호화에 실패했습니다.");
				log.error(">>> {}", encodedPassword, de);
			}
			default -> setForUnknown(request, "로그인 처리중 문제가 발생하였습니다.");
		}
		log.error(">>> code: {}, errMsg: {}", request.getAttribute("code"), request.getAttribute("errMsg"));
		String errMsg = URLEncoder.encode(request.getAttribute("errMsg").toString(), StandardCharsets.UTF_8);
		
		String encodedUrl = response.encodeURL(forwardUrl + "?code=" + request.getAttribute("code") + "&errMsg=" + errMsg);
		response.sendRedirect(encodedUrl);
	}
	
	private void setForUsernameNotFound(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.USERNAME_NOT_FOUND);
		request.setAttribute("errMsg", message);
	}
	
	private void setForPwdNotMatch(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.PASSWORD_NOT_MATCH);
		request.setAttribute("errMsg", message);
	}
	
	private void setForExpiredAccount(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.EXPIRED_ACCOUNT);
		request.setAttribute("errMsg", message);
	}
	
	private void setForLoginFailCoExceed(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.LOGIN_FAILR_COUNT);
		request.setAttribute("errMsg", message);
	}
	
	private void setForNotConfirmed(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.VERIFY_NOT_COMPLETE);
		request.setAttribute("errMsg", message);
	}
	
	private void setForAccessDenied(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.ACCESS_DENIED);
		request.setAttribute("errMsg", message);
	}
	
	private void setForUnknown(HttpServletRequest request, String message) {
		request.setAttribute("code", LoginResultCode.UNKNOWN);
		request.setAttribute("errMsg", message);
	}
	
	private void setForSuccess(HttpServletRequest request) {
		request.setAttribute("code", LoginResultCode.SUCCESS);
	}
	
}
