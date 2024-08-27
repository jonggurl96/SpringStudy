package com.demo.spring.config.security.aop;


import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Aspect
@Component
@RequiredArgsConstructor
public class EncryptAop {
	
	private final RSAGenHelper rsaGenHelper;
	
	@Before(
			value = "@annotation(com.demo.spring.config.security.annotation.RsaReady) && args(model, session, ..)"
			, argNames = "model,session")
	public void generateRSAVO(Model model, HttpSession session) throws NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
		RSAVO rsa = rsaGenHelper.generate();
		
		rsaGenHelper.setWebAttr(rsa, session);
		
		model.addAttribute("PUB", new String(Base64.getEncoder().encode(rsa.publicKey().getEncoded()),
		                                     StandardCharsets.UTF_8));
		model.addAttribute("e", rsa.getEncodedExponent());
		model.addAttribute("n", rsa.getEncodedModulus());
	}
	
}
