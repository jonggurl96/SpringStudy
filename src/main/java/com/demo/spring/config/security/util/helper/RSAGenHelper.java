package com.demo.spring.config.security.util.helper;


import com.demo.spring.config.security.exception.dec.RsaGenerateException;
import com.demo.spring.config.security.util.vo.RSAVO;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
public class RSAGenHelper extends DecoderGenHelper<RSAVO> {
	
	public RSAGenHelper(int KEY_SIZE, int RADIX_MODULUS, int RADIX_EXPONENT, String ATTR_KEY, String ATTR_MOD, String ATTR_EXP) {
		super(KEY_SIZE, RADIX_MODULUS, RADIX_EXPONENT, ATTR_KEY, ATTR_MOD, ATTR_EXP);
	}
	
	@Override
	public RSAVO generate(int rsaKeySize, int rsaRadixModulus, int rsaRadixExponent) {
		try {
			RSAVO rsavo = RSAVO.generate(rsaKeySize, rsaRadixModulus, rsaRadixExponent);
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
	
}
