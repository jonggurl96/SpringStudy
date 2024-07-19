package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.util.vo.DecoderVO;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class DecoderGenHelper<T extends DecoderVO> {
	
	private final int KEY_SIZE;
	
	private final int RADIX_MODULUS;
	
	private final int RADIX_EXPONENT;
	
	private final String ATTR_KEY;
	
	private final String ATTR_MOD;
	
	private final String ATTR_EXP;

	
	public void setRsaWebAttr(@NonNull T t, @NonNull HttpSession session, @NonNull Map<String, Object> map) {
		session.setAttribute(ATTR_KEY, t.privateKey());
		map.put(ATTR_MOD, t.base64Modulus());
		map.put(ATTR_EXP, t.base64Exponent());
	}
	
	public void setRsaWebAttr(@NonNull T t, @NonNull HttpSession session, @NonNull Model model) {
		Map<String, Object> map = new HashMap<>(2);
		setRsaWebAttr(t, session, map);
		model.addAllAttributes(map);
	}
	
	public T generate() {
		return generate(KEY_SIZE, RADIX_MODULUS, RADIX_EXPONENT);
	}
	
	protected abstract T generate(int rsaKeySize, int rsaRadixModulus, int rsaRadixExponent);
	
}
