package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.util.properties.AESProperties;
import com.demo.spring.config.security.util.vo.AESVO;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

@Slf4j
public class AESGenHelper extends DecoderGenHelper<AESVO> {
	
	private final String ATTR_IV;
	
	private final String ATTR_SALT;
	
	private final String SALT;
	
	public AESGenHelper(AESProperties properties) {
		super(properties.getKeySize(), properties.getAttrKey());
		ATTR_IV = properties.getAttrIv();
		ATTR_SALT = properties.getAttrSalt();
		SALT = properties.getSalt();
	}
	
	@Override
	public void setWebAttr(@NonNull AESVO aesvo, @NonNull HttpSession session, @NonNull Model model) {
		session.setAttribute(ATTR_KEY, aesvo);
		model.addAttribute(ATTR_IV, aesvo.base64Iv());
		model.addAttribute(ATTR_SALT, aesvo.base64Salt());
		addType(model, "AES");
	}
	
	@Override
	protected AESVO generate(int rsaKeySize) {
		AESVO vo = AESVO.generate(rsaKeySize, SALT);
		log.debug(">>> Decoder Record AESVO Generated.\n{}", vo);
		return vo;
	}
	
	@Override
	public AESVO getSessionAttr(HttpSession session) {
		return (AESVO) super.getSessionAttr(session);
	}
	
}
