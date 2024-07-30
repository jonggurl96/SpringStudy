package com.demo.spring.config.security.util.vo;


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
import java.util.Date;

public record AESVO(String privateKey, IvParameterSpec iv, String salt) implements DecoderVO {
	
	public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	
	public String base64Iv() {
		return base64EncodeToString(iv.getIV());
	}
	
	public String base64Salt() {
		return base64EncodeToString(salt.getBytes(StandardCharsets.UTF_8));
	}
	
	public static AESVO generate(int rsaKeySize, String salt) {
		SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes(StandardCharsets.UTF_8));
		byte[] allRandomSeed = secureRandom.toString().getBytes();
		int offset = 0;
		int len = rsaKeySize;
		
		if(allRandomSeed.length < rsaKeySize) {
			len = allRandomSeed.length;
			offset = rsaKeySize - len;
		}
		
		byte[] paddedSeed = new byte[rsaKeySize];
		System.arraycopy(allRandomSeed, 0, paddedSeed, offset, len);
		
		String privateKey = new String(paddedSeed, StandardCharsets.UTF_8);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(privateKey.substring(0, 16).getBytes());
		
		return new AESVO(privateKey, ivParameterSpec, salt);
	}
	
	@Override
	public String decrypt(String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		SecretKeySpec secretKeySpec = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "AES");
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
		
		byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
		return new String(decrypted, StandardCharsets.UTF_8);
	}
	
}
