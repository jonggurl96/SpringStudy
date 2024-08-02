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

public record AESVO(String secretKeyString, IvParameterSpec iv) implements DecoderVO {
	
	public static final String ALGORITHM_FULL = "AES/CBC/PKCS5Padding";
	
	public static final String ALGORITHM = "AES";
	
	private static final String KEY = "AESVO_ENCRYPTION__DECRYPTION_KEY";
	
	/**
	 * _INITIAL_VECTOR_ MD5 HASHING 84a82539b0e2e85d954faaa84acc34a1
	 */
	public static final String IV = "84a82539b0e2e85d";
	
	public String base64Iv() {
		return base64EncodeToString(iv.getIV());
	}
	
	public static AESVO generate() {
		return generate(KEY);
	}
	
	public static AESVO generate(String secretKeyString) {
		IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
		return new AESVO(secretKeyString, ivParameterSpec);
	}
	
	@Override
	public String decrypt(String encrypted) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8), ALGORITHM);
			
			Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			
			byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
			return new String(decrypted, StandardCharsets.UTF_8);
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
