package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.util.vo.CryptoVO;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class CryptoGenHelper<T extends CryptoVO> {
	
	protected final int KEY_SIZE;
	
	protected final String ATTR_KEY;
	
	public abstract void setWebAttr(@NonNull T t, @NonNull HttpSession session, @NonNull Model model);
	
	public CryptoVO getSessionAttr(HttpSession session) {
		return (CryptoVO) session.getAttribute(ATTR_KEY);
	}
	
	@SuppressWarnings({"unchecked"})
	protected void addType(Model model, String type) {
		List<String> encTypeList = (List<String>) model.getAttribute("encTypes");
		if(encTypeList == null || encTypeList.isEmpty())
			encTypeList = new ArrayList<>();
		encTypeList.add(type);
		model.addAttribute("encTypes", encTypeList);
	}
	
	public T generate() {
		return generate(KEY_SIZE);
	}
	
	protected abstract T generate(int rsaKeySize);
	
}
