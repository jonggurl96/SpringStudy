package com.demo.spring.config.security.aop;


import com.demo.spring.config.security.util.helper.RSAGenHelper;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EncryptAop {
	
	private final RSAGenHelper genHelper;
	
	@Before(value = "@annotation(com.demo.spring.config.security.annotation.RsaEncrypt) && args(model, session, ..)", argNames = "model,session")
	public void generateRSAVO(Model model, HttpSession session) throws Exception {
		RSAVO vo = genHelper.generate();
		genHelper.setRsaWebAttr(vo, session, model);
		log.debug(">>> RSAGenHelper generate RSAVO");
	}
	
}
