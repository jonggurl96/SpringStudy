package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.exception.dec.RsaGenerateException;
import com.demo.spring.config.security.util.properties.RSAProperties;
import com.demo.spring.config.security.util.vo.RSAVO;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
public class RSAGenHelper extends CryptoGenHelper<RSAVO> {
	
	public RSAGenHelper(RSAProperties properties) {
		super(properties.getKeySize(), properties.getAttrKey());
	}
	
	@Override
	public RSAVO generate(int rsaKeySize) {
		try {
			RSAVO rsavo = RSAVO.generate(rsaKeySize);
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
	public void setWebAttr(@NonNull RSAVO rsavo, @NonNull HttpSession session) {
		session.setAttribute(ATTR_KEY, rsavo);
	}
	
	@Override
	public RSAVO getSessionAttr(HttpSession session) {
		return (RSAVO) super.getSessionAttr(session);
	}
	
}
