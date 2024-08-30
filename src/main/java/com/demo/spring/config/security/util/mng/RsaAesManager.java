package com.demo.spring.config.security.util.mng;


import com.demo.spring.config.security.exception.dec.CryptoException;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;
import java.util.Map;

/**
 * RsaAesManager.java
 * <pre>
 * AES 키 값을 입력받아 RSA로 암/복호화
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 8. 29.
 */
@Slf4j
public class RsaAesManager extends CryptoManager {
	
	private final RSAPublicKey RSA_PUBLIC_KEY;
	
	private final RSAPrivateKey RSA_PRIVATE_KEY;
	
	private final String RSA_NAME;
	
	private final String RSA_MD;
	
	private final String RSA_MGF;
	
	private final String AES_NAME;
	
	private final byte[] AESIV;
	
	public static final String SEPERATOR = "RSAES";
	
	private byte[] AES_SECRET_KEY;
	
	public RsaAesManager(RsaAesProperties properties) throws CryptoException {
		super();
		
		RSA_NAME = properties.getRsaName();
		RSA_MD = properties.getRsaMd();
		RSA_MGF = properties.getRsaMgf();
		AES_NAME = properties.getAesName();
		
		KeyPair keyPair = generateKeyPair(properties.getRsaKeySize());
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		this.RSA_PRIVATE_KEY = (RSAPrivateKey) privateKey;
		this.RSA_PUBLIC_KEY = (RSAPublicKey) publicKey;
		
		AESIV = new byte[properties.getAesKeySize() / 2];
		new SecureRandom().nextBytes(AESIV);
	}
	
	
	private KeyPair generateKeyPair(int keySize) throws CryptoException {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(keySize);
			return keyPairGenerator.generateKeyPair();
		} catch(NoSuchAlgorithmException nae) {
			throw new CryptoException("RSA 알고리즘 정보를 불러오는 데 실패했습니다.", nae);
		}
	}
	
	@Override
	public String decrypt(String text) throws CryptoException {
		String[] keyAndText = text.split(SEPERATOR);
		byte[] aesKeyBytes = decryptAesKey_RSA(keyAndText[0]);
		AES_SECRET_KEY = aesKeyBytes;
		return decryptMessage(aesKeyBytes, keyAndText[1]);
	}
	
	public byte[] decryptAesKey_RSA(String encoded) {
		int mode = Cipher.DECRYPT_MODE;
		OAEPParameterSpec spec = new OAEPParameterSpec(RSA_MD,
		                                               RSA_MGF,
		                                               new MGF1ParameterSpec(RSA_MD),
		                                               PSource.PSpecified.DEFAULT);
		
		Cipher cipher = initCipher(RSA_NAME, mode, RSA_PRIVATE_KEY, spec);
		return crypt(cipher, encoded, this::base64HexTobytes);
	}
	
	public String decryptMessage(byte[] aesKey, String text) {
		int mode = Cipher.DECRYPT_MODE;
		SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(AESIV);
		
		Cipher cipher = initCipher(AES_NAME, mode, key, ivParameterSpec);
		return cryptToString(cipher, text, this::base64HexTobytes);
	}
	
	public String decryptAES(String text) {
		return decryptMessage(AES_SECRET_KEY, text);
	}
	
	private byte[] base64HexTobytes(String base64Hex) {
		String hexString = new String(Base64.getDecoder().decode(base64Hex), StandardCharsets.UTF_8);
		int byteSize = hexString.length() / 2;
		byte[] bytes = new byte[byteSize];
		
		for(int i = 0; i < byteSize; i++) {
			bytes[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
		}
		return bytes;
	}
	
	private String bytesToBase64Hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return Base64.getEncoder().encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public Map<String, String> getEncodedPropsMap() {
		byte[] n = RSA_PUBLIC_KEY.getModulus().toByteArray();
		byte[] e = RSA_PUBLIC_KEY.getPublicExponent().toByteArray();
		return Map.of("n",
		              bytesToBase64Hex(n),
		              "e",
		              Base64.getEncoder().encodeToString(e),
		              "iv",
		              bytesToBase64Hex(AESIV));
	}
	
}
