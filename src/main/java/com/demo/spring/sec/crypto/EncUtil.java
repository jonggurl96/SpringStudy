package com.demo.spring.sec.crypto;


import com.demo.spring.config.security.util.helper.AESGenHelper;
import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.vo.AESVO;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Setter
@Component
@RequiredArgsConstructor
public class EncUtil {
	
	private static final String delimiter = "--==AES==--";
	
	private final AESGenHelper aesGenHelper;
	
	private final RSAGenHelper rsaGenHelper;
	
	private HttpSession session;
	
	public String decrypt(String cipher) {
		String[] tokens = cipher.split(delimiter);
		String cipherText = new String(Base64.getDecoder().decode(tokens[0]), StandardCharsets.UTF_8);
		String cipherKey = new String(Base64.getDecoder().decode(tokens[1]), StandardCharsets.UTF_8);
		
		RSAVO rsavo = rsaGenHelper.getSessionAttr(session);
		String aesKey = rsavo.decrypt(cipherKey);
		
		AESVO aesvo = aesGenHelper.generateWithKey(aesKey);
		return aesvo.decrypt(cipherText);
	}
	
}
