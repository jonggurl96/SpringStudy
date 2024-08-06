package com.demo.spring.sec.crypto;


import com.demo.spring.config.security.util.helper.AESGenHelper;
import com.demo.spring.config.security.util.vo.AESVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;

@Setter
@Component
@RequiredArgsConstructor
public class EncUtil {
	
	private final AESGenHelper aesGenHelper;
	
	public String decrypt(HttpSession session, String cipher) {
		int cryptMode = Cipher.DECRYPT_MODE;
		AESVO aesvo = aesGenHelper.getSessionAttr(session);
		
		return aesvo.crypt(cryptMode, cipher);
	}
	
}
