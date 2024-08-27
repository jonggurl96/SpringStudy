package com.demo.spring.config.security.util.vo;


import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface CryptoVO {
	
	public default String base64EncodeToString(byte[] bytes) {
		byte[] encoded = Base64.getEncoder().encode(bytes);
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public default String base64DecodeToString(byte[] bytes) {
		byte[] decoded = Base64.getDecoder().decode(bytes);
		return new String(decoded, StandardCharsets.UTF_8);
	}
	
	public default byte[] base64Encode(String text) {
		return Base64.getEncoder().encode(text.getBytes(StandardCharsets.UTF_8));
	}
	
	public default byte[] base64Decode(String text) {
		String hexString = new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
		int byteSize = hexString.length() / 2;
		byte[] bytes = new byte[byteSize];

		for(int i = 0; i < byteSize; i++) {
			String sub = hexString.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(sub, 16);
		}

		return bytes;
	}
	
	public default byte[] getCryptBytes(int cryptMode, String text) {
		return cryptMode == Cipher.DECRYPT_MODE ? base64Decode(text) : text.getBytes(StandardCharsets.UTF_8);
	}
	
	public default String getCryptResult(int cryptMode, byte[] bytes) {
		return cryptMode == Cipher.ENCRYPT_MODE ? base64EncodeToString(bytes)
		                                        : new String(bytes, StandardCharsets.UTF_8);
	}
	
	public abstract String crypt(int cryptMode, String text);
	
	public default String encrypt(String text) {
		return crypt(Cipher.ENCRYPT_MODE, text);
	}
	
	public default String decrypt(String text) {
		return crypt(Cipher.DECRYPT_MODE, text);
	}
	
}
