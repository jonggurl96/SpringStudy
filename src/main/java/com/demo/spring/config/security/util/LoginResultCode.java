package com.demo.spring.config.security.util;


/**
 * LoginResultCode.java
 * <pre>
 * 인증 실패 오류 코드
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 16.
 */
public class LoginResultCode {
	
	/**
	 * 존재하지 않는 사용자
	 */
	public static final String USERNAME_NOT_FOUND = "000";
	
	/**
	 * 비밀번호 입력 오류
	 */
	public static final String PASSWORD_NOT_MATCH = "001";
	
	/**
	 * 탈퇴한 회원
	 */
	public static final String EXPIRED_ACCOUNT = "002";
	
	/**
	 * 로그인 실패 횟수 초과
	 */
	public static final String LOGIN_FAILR_COUNT = "003";
	
	/**
	 * 인증 필요
	 */
	public static final String VERIFY_NOT_COMPLETE = "005";
	
	/**
	 * 접근 불가
	 */
	public static final String ACCESS_DENIED = "009";
	
	/**
	 * 알 수 없는 오류
	 */
	public static final String UNKNOWN = "998";
	
	/**
	 * 정상 로그인
	 */
	public static final String SUCCESS = "999";
	
}
