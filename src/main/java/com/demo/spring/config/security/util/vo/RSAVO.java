package com.demo.spring.config.security.util.vo;


import com.demo.spring.config.security.exception.dec.CryptoException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
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
@Slf4j
public record RSAVO(PrivateKey privateKey, PublicKey publicKey) implements CryptoVO {
	
	public static final String ALGORITHM = "RSA";
	
	public static final String ALGORITHM_FULL = "RSA/ECB/OAEPPadding";
	
	public static final String HASHING = "SHA-256";
	
	public static final String MD_NAME = "MGF1";
	
	public static RSAVO generate(int keySize) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(keySize);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		return new RSAVO(privateKey, publicKey);
	}
	
	@Override
	public String crypt(int cryptMode, String text) {
		Cipher cipher;
		
		try {
			cipher = Cipher.getInstance(ALGORITHM_FULL);
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new CryptoException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		}
		
		OAEPParameterSpec spec = new OAEPParameterSpec(HASHING, MD_NAME, new MGF1ParameterSpec(HASHING),
		                                               PSource.PSpecified.DEFAULT);
		
		Key key = cryptMode == Cipher.ENCRYPT_MODE ? publicKey : privateKey;
		
		try {
			cipher.init(cryptMode, key, spec);
		} catch(InvalidKeyException ike) {
			throw new CryptoException("사용할 수 없는 키입니다.");
		} catch(InvalidAlgorithmParameterException iape) {
			throw new CryptoException("알고리즘 ParameterSpec이 잘못되었습니다.", iape);
		}
		
		try {
			byte[] bytes = getCryptBytes(cryptMode, text);
			byte[] decrypted = cipher.doFinal(bytes);
			
			return getCryptResult(cryptMode, decrypted);
		} catch(IllegalBlockSizeException ibe) {
			throw new CryptoException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new CryptoException("Padding이 잘못되었습니다.", bpe);
		}
	}
	
	public String getEncodedExponent() {
		return Base64.getUrlEncoder().encodeToString(((RSAPublicKey) publicKey).getPublicExponent().toByteArray());
	}
	
	public String getEncodedModulus() {
		byte[] bytes = ((RSAPublicKey) publicKey).getModulus().toByteArray();
		log.debug(">>> RSA Public Modulus Bytes: {}", bytes);
		return byteToBase64Encode(bytes);
	}
	
	private String byteToBase64Encode(byte[] bytes) {
		StringBuilder hexStringBuilder = new StringBuilder();
		for(byte b : bytes) {
			hexStringBuilder.append(String.format("%02x", b));
		}
		String hexString = hexStringBuilder.toString();
		
		return Base64.getEncoder().encodeToString(hexString.getBytes(StandardCharsets.UTF_8));
	}
	
}
