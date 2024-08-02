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
	
	public AESGenHelper(AESProperties properties) {
		super(properties.getKeySize(), properties.getAttrKey());
		ATTR_IV = properties.getAttrIv();
	}
	
	@Override
	public void setWebAttr(@NonNull AESVO aesvo, @NonNull HttpSession session, @NonNull Model model) {
		model.addAttribute(ATTR_IV, aesvo.base64Iv());
		addType(model, "AES");
	}
	
	@Override
	protected AESVO generate(int rsaKeySize) {
		AESVO vo = AESVO.generate();
		log.debug(">>> Decoder Record AESVO Generated.\n{}", vo);
		log.debug(">>> AESVO Initial Vector: {}", vo.iv().getIV());
		return vo;
	}
	
	public AESVO generateWithKey(String key) {
		return AESVO.generate(key);
	}
	
}
