package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.exception.dec.RsaGenerateException;
import com.demo.spring.config.security.util.properties.RSAProperties;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

@Slf4j
public class RSAGenHelper extends DecoderGenHelper<RSAVO> {
	
	private final int RADIX_MODULUS;
	
	private final int RADIX_EXPONENT;
	
	private final String ATTR_MOD;
	
	private final String ATTR_EXP;
	
	public RSAGenHelper(RSAProperties properties) {
		super(properties.getKeySize(), properties.getAttrKey());
		RADIX_MODULUS = properties.getRadixModulus();
		RADIX_EXPONENT = properties.getRadixExponent();
		ATTR_MOD = properties.getAttrMod();
		ATTR_EXP = properties.getAttrExp();
	}
	
	@Override
	public RSAVO generate(int rsaKeySize) {
		try {
			RSAVO rsavo = RSAVO.generate(rsaKeySize, RADIX_MODULUS, RADIX_EXPONENT);
			log.debug(">>> Decoder Record RSAVO Generated.\n{}", rsavo);
			return rsavo;
		} catch(NoSuchAlgorithmException ignored) {
			log.error(">>> [RSA] NoSuchAlgorithmException.");
			throw new RsaGenerateException(">>> [RSA] NoSuchAlgorithmException.");
		} catch(InvalidKeySpecException ikse) {
			log.error(">>> [RSA] InvalidKeySpecException.");
			throw new RsaGenerateException(">>> [RSA] InvalidKeySpecException.", ikse);
		}
	}
	
	@Override
	public void setWebAttr(@NonNull RSAVO rsavo, @NonNull HttpSession session, @NonNull Model model) {
		session.setAttribute(ATTR_KEY, rsavo);
		HashMap<String, String> modelAttr = new HashMap<>(2);
		modelAttr.put(ATTR_MOD, rsavo.base64Modulus());
		modelAttr.put(ATTR_EXP, rsavo.base64Exponent());
		super.put(model, "RSA", modelAttr);
	}
	
	@Override
	public RSAVO getSessionAttr(HttpSession session) {
		return (RSAVO) super.getSessionAttr(session);
	}
	
}
