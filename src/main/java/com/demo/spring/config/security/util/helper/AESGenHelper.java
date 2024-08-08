package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.util.properties.AESProperties;
import com.demo.spring.config.security.util.vo.AESVO;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

@Slf4j
public class AESGenHelper extends CryptoGenHelper<AESVO> {
	
	public AESGenHelper(AESProperties properties) {
		super(properties.getKeySize(), properties.getAttrKey());
	}
	
	@Override
	public void setWebAttr(@NonNull AESVO aesvo, @NonNull HttpSession session) {
		session.setAttribute(ATTR_KEY, aesvo);
	}
	
	@Override
	protected AESVO generate(int rsaKeySize) {
		AESVO vo = AESVO.generate(rsaKeySize);
		log.debug(">>> Decoder Record AESVO Generated.\n{}", vo);
		return vo;
	}
	
	public void setEncryptedKey(String key, Model model) {
		model.addAttribute(ATTR_KEY, key);
	}
	
	@Override
	public AESVO getSessionAttr(HttpSession session) {
		return (AESVO) super.getSessionAttr(session);
	}
	
}
