package com.demo.spring.config.security.util.vo;


import com.demo.spring.config.security.exception.dec.CryptoException;

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
import java.security.SecureRandom;
import java.util.Arrays;

public record AESVO(String key, String iv) implements CryptoVO {
	
	public static final String ALGORITHM = "AES";
	
	public static final String ALGORITHM_FULL = "AES/CBC/PKCS5Padding";
	
	public static AESVO importVO(String key, String iv) {
		return new AESVO(key, iv);
	}
	
	public static AESVO generate(int rsaKeySize) {
		SecureRandom random = new SecureRandom();
		
		byte[] keyBytes = new byte[rsaKeySize];
		random.nextBytes(keyBytes);
		
		byte[] ivBytes = new byte[rsaKeySize / 2];
		random.nextBytes(ivBytes);
		
		return new AESVO(Arrays.toString(keyBytes), Arrays.toString(ivBytes));
	}
	
	@Override
	public String crypt(int cryptMode, String text) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
			
			Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
			cipher.init(cryptMode, secretKeySpec, ivParameterSpec);
			
			byte[] bytes = getCryptBytes(cryptMode, text);
			
			byte[] decrypted = cipher.doFinal(bytes);
			
			return getCryptResult(cryptMode, decrypted);
			
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new CryptoException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		} catch(InvalidKeyException ike) {
			throw new CryptoException("사용할 수 없는 키입니다.");
		} catch(IllegalBlockSizeException ibe) {
			throw new CryptoException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new CryptoException("Padding이 잘못되었습니다.", bpe);
		} catch(InvalidAlgorithmParameterException iape) {
			throw new CryptoException("ParameterSpec이 잘못되었습니다.", iape);
		}
	}
	
}
