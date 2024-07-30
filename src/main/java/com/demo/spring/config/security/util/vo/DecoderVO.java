package com.demo.spring.config.security.util.vo;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface DecoderVO {
	
	public default String base64EncodeToString(byte[] bytes) {
		byte[] encoded = Base64.getEncoder().encode(bytes);
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public default byte[] hexToBytes(String hex) {
		if(hex == null || hex.isEmpty() || hex.length() % 2 != 0) {
			return new byte[]{};
		}
		
		byte[] bytes = new byte[hex.length() / 2];
		
		for(int l = 0; l < hex.length(); l += 2) {
			bytes[l / 2] = (byte) Integer.parseInt(hex.substring(l, l + 2), 16);
		}
		return bytes;
	}
	
	public abstract String decrypt(String encrypted) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException;
	
}
