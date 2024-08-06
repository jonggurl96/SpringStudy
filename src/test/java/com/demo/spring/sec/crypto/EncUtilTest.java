package com.demo.spring.sec.crypto;


import com.demo.spring.config.security.util.vo.AESVO;
import com.demo.spring.config.security.util.vo.RSAVO;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SpringJUnitConfig
class EncUtilTest {
	
	@Test
	void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
		AESVO aesvo = AESVO.generate();
		RSAVO rsavo = RSAVO.generate(2048, 16, 16);
		
		String text = "테스트 문장";
		
		String cipher = aesvo.encode(text);
		System.out.println("AES encrypt: " + cipher);
		
		String encryptedAesKey = rsavo.encrypt(aesvo.secretKeyString());
		System.out.println("RSA encrypt AES key: " + encryptedAesKey);
		
		String decryptedAesKey = rsavo.decrypt(encryptedAesKey);
		System.out.println("RSA decrypted AES key: " + decryptedAesKey);
		
		String decryptedText = AESVO.generate(decryptedAesKey).encode(cipher);
		System.out.println("AES decrypt: " + decryptedText);
	}
	
}