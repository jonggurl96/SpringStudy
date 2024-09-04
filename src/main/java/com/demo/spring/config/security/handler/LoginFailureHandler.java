package com.demo.spring.config.security.handler;


import com.demo.spring.config.security.exception.AccessDeniedException;
import com.demo.spring.config.security.exception.LoginFailedTooMuchException;
import com.demo.spring.config.security.exception.NotConfirmedException;
import com.demo.spring.config.security.exception.PasswordNotMatchException;
import com.demo.spring.config.security.exception.dec.CryptoException;
import com.demo.spring.config.security.util.LoginResultCode;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private final String forwardUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException exception) throws IOException, ServletException {
		String errParameter = switch(exception) {
			case InternalAuthenticationServiceException isae -> switch(isae.getCause()) {
				case UsernameNotFoundException ignored ->
						setForUsernameNotFound() + "입력한 아이디나 비밀번호에 적합한 사용자가 존재하지 않습니다.";
				case LoginFailedTooMuchException ignored -> setForLoginFailCoExceed() + "로그인 시도 횟수를 초과한 계정입니다.";
				case NotConfirmedException ignored -> setForNotConfirmed() + "인증이 완료되지 않은 계정입니다.";
				case AccountExpiredException ignored -> setForExpiredAccount() + "탈퇴한 사용자입니다.";
				default -> setForUnknown(isae) + "알 수 없는 오류가 발생했습니다.";
			};
			case PasswordNotMatchException pnme ->
					setForPwdNotMatch() + String.format("로그인 시도가 %d회 초과되었습니다.\n%d회 초과 시 본인인증을 거쳐야만 로그인 가능합니다.",
					                                    pnme.getTryCnt(),
					                                    pnme.getMaxTryCnt());
			case BadCredentialsException ignored -> setForUsernameNotFound() + "사용자 정보가 정확하지 않습니다.";
			case UsernameNotFoundException ignored -> setForUsernameNotFound() + "사용자 정보가 존재하지 않습니다.";
			case AccountExpiredException ignored -> setForExpiredAccount() + "탈퇴한 사용자입니다.";
			case LoginFailedTooMuchException ignored -> setForLoginFailCoExceed() + "로그인 시도 횟수를 초과한 계정입니다.";
			case NotConfirmedException ignored -> setForNotConfirmed() + "인증이 완료되지 않은 계정입니다.";
			case AccessDeniedException ignored -> setForAccessDenied() + "접근이 거부되었습니다.";
			case CryptoException ignored -> setForPwdNotMatch() + "패스워드 복호화에 실패했습니다.";
			default -> setForUnknown(exception) + "로그인 처리중 문제가 발생하였습니다.";
		};
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl + errParameter);
		dispatcher.forward(request, response);
	}
	
	private String setForUsernameNotFound() {
		return formatParam(LoginResultCode.USERNAME_NOT_FOUND);
	}
	
	private String setForPwdNotMatch() {
		return formatParam(LoginResultCode.PASSWORD_NOT_MATCH);
	}
	
	private String setForExpiredAccount() {
		return formatParam(LoginResultCode.EXPIRED_ACCOUNT);
	}
	
	private String setForLoginFailCoExceed() {
		return formatParam(LoginResultCode.LOGIN_FAILR_COUNT);
	}
	
	private String setForNotConfirmed() {
		return formatParam(LoginResultCode.VERIFY_NOT_COMPLETE);
	}
	
	private String setForAccessDenied() {
		return formatParam(LoginResultCode.ACCESS_DENIED);
	}
	
	private String setForUnknown(AuthenticationException exception) {
		log.error(">>> Login Failed.", exception);
		return formatParam(LoginResultCode.UNKNOWN);
	}
	
	private String formatParam(String code) {
		return "?code=" + code + "&errMsg=";
	}
	
}
