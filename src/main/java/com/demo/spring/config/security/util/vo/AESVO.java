package com.demo.spring.config.security.util.vo;


import com.demo.spring.config.security.exception.dec.DecryptException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public record AESVO(SecretKeySpec key, IvParameterSpec iv) implements CryptoVO {
	
	public static final String ALGORITHM = "AES";
	
	/**
	 * AESVO_ENCRYPTION__DECRYPTION_KEY MD5 HASHING f3ab59b339c6a34d2c58b78a0d33a5d9
	 */
	public static final String KEY = "f3ab59b339c6a34d2c58b78a0d33a5d9";
	
	/**
	 * _INITIAL_VECTOR_ MD5 HASHING 84a82539b0e2e85d954faaa84acc34a1
	 */
	public static final String IV = "84a82539b0e2e85d";
	
	public String base64Iv() {
		return base64EncodeToString(iv.getIV());
	}
	
	public String base64Key() {
		return base64EncodeToString(KEY.getBytes(StandardCharsets.UTF_8));
	}
	
	public static AESVO generate() {
		IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
		return new AESVO(secretKey, ivParameterSpec);
	}
	
	@Override
	public String crypt(int cryptMode, String text) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(cryptMode, key, iv);
			
			byte[] bytes = getCryptBytes(cryptMode, text);
			
			byte[] decrypted = cipher.doFinal(bytes);
			return getCryptResult(cryptMode, decrypted);
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new DecryptException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		} catch(InvalidKeyException ike) {
			throw new DecryptException("사용할 수 없는 키입니다.");
		} catch(IllegalBlockSizeException ibe) {
			throw new DecryptException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new DecryptException("Padding이 잘못되었습니다.", bpe);
		} catch(InvalidAlgorithmParameterException iape) {
			throw new DecryptException("ParameterSpec이 잘못되었습니다.", iape);
		}
	}
	
}
