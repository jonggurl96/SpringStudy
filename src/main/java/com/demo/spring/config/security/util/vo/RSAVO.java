package com.demo.spring.config.security.util.vo;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSAVO.java
 * <pre>
 * RSA 복호화 레코드
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 7. 18.
 */
@Slf4j
public record RSAVO(PrivateKey privateKey, String modulus, String exponent, PublicKey publicKey) implements DecoderVO {
	
	public static final String ALGORITHM = "RSA";
	
	public String base64Modulus() {
		return base64EncodeToString(modulus.getBytes());
	}
	
	public String base64Exponent() {
		return base64EncodeToString(exponent.getBytes());
	}
	
	public static RSAVO generate(int keySize, int modulusRadix, int exponentRadix) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(keySize, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		
		RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String modulus = publicKeySpec.getModulus().toString(modulusRadix);
		String exponent = publicKeySpec.getPublicExponent().toString(exponentRadix);
		
		return new RSAVO(privateKey, modulus, exponent, publicKey);
	}
	
	@Override
	public String decrypt(String encrypted) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
		return new String(decrypted, StandardCharsets.UTF_8);
	}
	
	public String encrypt(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		return new String(encrypted, StandardCharsets.UTF_8);
	}
	
}
