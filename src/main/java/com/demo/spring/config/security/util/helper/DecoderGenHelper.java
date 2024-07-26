package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.util.vo.DecoderVO;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class DecoderGenHelper<T extends DecoderVO> {
	
	protected final int KEY_SIZE;
	
	protected final int RADIX_MODULUS;
	
	protected final int RADIX_EXPONENT;
	
	protected final String ATTR_KEY;
	
	protected final String ATTR_MOD;
	
	protected final String ATTR_EXP;
	
	protected final String ATTR_PUB;
	
	public abstract void setRsaWebAttr(@NonNull T t, @NonNull HttpSession session, @NonNull Model model);
	
	public DecoderVO getSessionAttr(HttpSession session) {
		DecoderVO decoder = (DecoderVO) session.getAttribute(ATTR_KEY);
		session.removeAttribute(ATTR_KEY);
		return decoder;
	}
	
	protected void put(Model model, String type, Map<String, String> attrMap) {
		model.addAttribute("encType", type);
		model.addAttribute("encAttr", attrMap);
	}
	
	public T generate() {
		return generate(KEY_SIZE, RADIX_MODULUS, RADIX_EXPONENT);
	}
	
	protected abstract T generate(int rsaKeySize, int rsaRadixModulus, int rsaRadixExponent);
	
}
