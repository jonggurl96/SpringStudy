package com.demo.spring.config.security.aop;


import com.demo.spring.config.security.util.mng.RsaAesManager;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
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
	
	private final RsaAesProperties rsaAesProperties;
	
	@Before(
			value = "@annotation(com.demo.spring.config.security.annotation.RsaAesReady) && args(model, session, ..)",
			argNames = "model,session")
	public void generateRSAVO(Model model, HttpSession session) throws NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
		
		RsaAesManager manager = new RsaAesManager(rsaAesProperties);
		model.addAllAttributes(manager.getEncodedPropsMap());
		session.setAttribute(manager.getSessionKey(), manager);
	}
	
}
