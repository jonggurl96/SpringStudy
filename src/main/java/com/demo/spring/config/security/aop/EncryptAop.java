package com.demo.spring.config.security.aop;


import com.demo.spring.config.security.util.vo.AESVO;
import com.demo.spring.config.security.util.vo.RSAVO;
import com.demo.spring.sec.crypto.EncUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Aspect
@Component
@RequiredArgsConstructor
public class EncryptAop {
	
	private final EncUtil encUtil;
	
	@Before(
			value = "@annotation(com.demo.spring.config.security.annotation.HybridEncrypt) && args(model, session, ..)"
			, argNames = "model,session")
	public void generateRSAVO(Model model, HttpSession session) throws NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
		RSAVO rsa = encUtil.getRsa();
		AESVO aes = encUtil.getAes();
		
		encUtil.getRsaGenHelper().setWebAttr(rsa, session);
		encUtil.getAesGenHelper().setWebAttr(aes, session);
		
		String cryptoParams = encUtil.getParamString(session);
		
		model.addAttribute("CRYPTO_PARAMS", cryptoParams);
	}
	
}
