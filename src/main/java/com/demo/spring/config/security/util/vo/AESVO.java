package com.demo.spring.config.security.util.vo;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public record AESVO(String secretKeyString, IvParameterSpec iv, String salt) implements DecoderVO {
	
	public static final String ALGORITHM_FULL = "AES/CBC/PKCS5Padding";
	
	public static final String ALGORITHM = "AES";
	
	private static final String KEY = "AESVO_ENCRYPTION__DECRYPTION_KEY";
	
	public String base64Iv() {
		return base64EncodeToString(iv.getIV());
	}
	
	public String base64Salt() {
		return base64EncodeToString(salt.getBytes(StandardCharsets.UTF_8));
	}
	
	public static AESVO generate(int rsaKeySize, String salt) {
		String key = KEY.length() > rsaKeySize ? KEY.substring(0, rsaKeySize) : KEY;
		IvParameterSpec ivParameterSpec =
				new IvParameterSpec(key.substring(0, rsaKeySize / 2).getBytes(StandardCharsets.UTF_8));
		return new AESVO(key, ivParameterSpec, salt);
	}
	
	@Override
	public String decrypt(String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		SecretKeySpec secretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8), ALGORITHM);
		
		Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		
		byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
		return new String(decrypted, StandardCharsets.UTF_8);
	}
	
}
