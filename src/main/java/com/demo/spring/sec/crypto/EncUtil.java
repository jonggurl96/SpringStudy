package com.demo.spring.sec.crypto;


import com.demo.spring.config.security.util.helper.AESGenHelper;
import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.vo.AESVO;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class EncUtil {
	
	private final AESGenHelper aesGenHelper;
	
	private final RSAGenHelper rsaGenHelper;
	
	private static final String DELIMITER = "[|<|NAMES|>|]";
	
	private static final String RSA_PREFIX = "-----BEGIN PRIVATE KEY-----";
	
	private static final String RSA_SUFFIX = "-----END PRIVATE KEY-----";
	
	public String getParamString(HttpSession session) {
		AESVO aesvo = aesGenHelper.getSessionAttr(session);
		RSAVO rsavo = rsaGenHelper.getSessionAttr(session);
		
		List<String> paramList = List.of(RSA_PREFIX,
		                                 Arrays.toString(rsavo.privateKey().getEncoded()),
		                                 RSA_SUFFIX,
		                                 aesvo.key(),
		                                 aesvo.iv());
		String params = String.join("|", paramList);
		
		List<String> paramNames = List.of("prfx", "rk", "sfx", "ak", "iv");
		SecureRandom random = new SecureRandom(params.getBytes(StandardCharsets.UTF_8));
		
		ArrayList<String> paramNameCodeList = new ArrayList<>(8);
		
		paramNames.forEach(name -> {
			String sb = "\"" + name
			            + "\": \""
			            + random.nextLong()
			            + "\"";
			paramNameCodeList.add(sb);
		});
		
		String ret = params + DELIMITER + "{" + String.join(", ", paramNameCodeList) + "}";
		log.debug(">>> CRYPTO_PARAMS: {}", ret);
		return Base64.getEncoder().encodeToString(ret.getBytes(StandardCharsets.UTF_8));
	}
	
	public AESVO getAes() {
		return aesGenHelper.generate();
	}
	
	public RSAVO getRsa() {
		return rsaGenHelper.generate();
	}
	
}
