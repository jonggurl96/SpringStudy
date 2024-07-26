package com.demo.spring.config.security.util.vo;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

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
public record RSAVO(PrivateKey privateKey, String modulus, String exponent, PublicKey publicKey) implements DecoderVO {
	
	public static final String ALGORITHM_RSA = "RSA";
	
	public byte[] getBModulus() {
		return modulus.getBytes();
	}
	
	public byte[] getBExponent() {
		return exponent.getBytes();
	}
	
	@Override
	public String base64Modulus() {
		return base64EncodeToString(getBModulus());
	}
	
	@Override
	public String base64Exponent() {
		return base64EncodeToString(getBExponent());
	}
	
	private String base64EncodeToString(byte[] bytes) {
		byte[] encoded = Base64.getEncoder().encode(bytes);
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public static RSAVO generate(int keySize, int modulusRadix, int exponentRadix) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
		keyPairGenerator.initialize(keySize, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		
		RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String modulus = publicKeySpec.getModulus().toString(modulusRadix);
		String exponent = publicKeySpec.getPublicExponent().toString(exponentRadix);
		
		return new RSAVO(privateKey, modulus, exponent, publicKey);
	}
	
	@Override
	public String decrypt(String encrypted) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
		return new String(decrypted, StandardCharsets.UTF_8);
	}
	
	private byte[] hexToBytes(String hex) {
		if(hex == null || hex.isEmpty() || hex.length() % 2 != 0) {
			return new byte[]{};
		}
		
		byte[] bytes = new byte[hex.length() / 2];
		
		for(int l = 0; l < hex.length(); l += 2) {
			bytes[l / 2] = (byte) Integer.parseInt(hex.substring(l, l + 2), 16);
		}
		
		return bytes;
	}
	
	
}
