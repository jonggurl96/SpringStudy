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
	
	protected final String ATTR_KEY;
	
	public abstract void setWebAttr(@NonNull T t, @NonNull HttpSession session, @NonNull Model model);
	
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
		return generate(KEY_SIZE);
	}
	
	protected abstract T generate(int rsaKeySize);
	
}
