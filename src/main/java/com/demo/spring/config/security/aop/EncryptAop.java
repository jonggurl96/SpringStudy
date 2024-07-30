package com.demo.spring.config.security.aop;


import com.demo.spring.config.security.util.helper.AESGenHelper;
import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.vo.AESVO;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EncryptAop {
	
	private final RSAGenHelper rsaHelper;
	
	private final AESGenHelper aesHelper;
	
	@Before(value = "@annotation(com.demo.spring.config.security.annotation.HybridEncrypt) && args(model, session, ..)"
			, argNames = "model,session")
	public void generateRSAVO(Model model, HttpSession session) throws NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
		RSAVO rsa = rsaHelper.generate();
		AESVO aes = aesHelper.generate();
		
		rsaHelper.setWebAttr(rsa, session, model);
		aesHelper.setWebAttr(aes, session, model);
		
		String encryptedSecretKey = rsa.encrypt(aes.privateKey());
		model.addAttribute("SECRET_KEY", encryptedSecretKey);
	}
	
}
