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
import java.util.Arrays;
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
	
	private final RsaAesProperties rsaAesProperties;
	
	private final RSAPublicKey RSA_PUBLIC_KEY;
	
	private final RSAPrivateKey RSA_PRIVATE_KEY;
	
	private final byte[] AES_IV;
	
	public static final String SEPERATOR = "RSAES";
	
	private final byte[] AES_SECRET_KEY;
	
	public RsaAesManager(RsaAesProperties properties) {
		super();
		
		rsaAesProperties = properties;
		
		KeyPair keyPair = generateRsaKeyPair(rsaAesProperties.getRsaKeySize());
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		this.RSA_PRIVATE_KEY = (RSAPrivateKey) privateKey;
		this.RSA_PUBLIC_KEY = (RSAPublicKey) publicKey;
		
		AES_SECRET_KEY = base64HexToBytes(rsaAesProperties.getAesKey());
		
		int ivSize = rsaAesProperties.getAesKeySize() / 2;
		AES_IV = new byte[ivSize];
		
		System.arraycopy(AES_SECRET_KEY, 0, AES_IV, 0, ivSize);
	}
	
	private KeyPair generateRsaKeyPair(int keySize) throws CryptoException {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(keySize);
			return keyPairGenerator.generateKeyPair();
		} catch(NoSuchAlgorithmException nae) {
			throw new CryptoException("RSA 알고리즘 정보를 불러오는 데 실패했습니다.", nae);
		}
	}
	
	/**
	 * <pre>
	 *     1. base64 decoding
	 *     2. RSA 복호화
	 *     3. 구분자로 분리: AES 키, AES 암호, AES IV
	 *     4. AES 복호화
	 * </pre>
	 *
	 * @param text
	 *
	 * @return
	 *
	 * @throws CryptoException AES 키, IV가 객체 멤버와 다름
	 */
	@Override
	public String decrypt(String text) throws CryptoException {
		String[] keyCryptoIv = decryptAesKey_RSA(RSA_PRIVATE_KEY, text);
		
		if(!Arrays.equals(base64HexToBytes(keyCryptoIv[0]), AES_SECRET_KEY))
			throw new CryptoException("잘못된 AES 키 값입니다.");
		
		if(!Arrays.equals(base64HexToBytes(keyCryptoIv[2]), AES_IV))
			throw new CryptoException("AES IV가 다른 값입니다.");
		
		return decryptAES(keyCryptoIv[1]);
	}
	
	/**
	 * @param key
	 * @param encoded
	 *
	 * @return String > RSA decrypt > AES 키, AES 암호, AES IV
	 */
	public String[] decryptAesKey_RSA(Key key, String encoded) {
		int mode = Cipher.DECRYPT_MODE;
		OAEPParameterSpec spec = new OAEPParameterSpec(rsaAesProperties.getRsaMd(),
		                                               rsaAesProperties.getRsaMgf(),
		                                               new MGF1ParameterSpec(rsaAesProperties.getRsaMd()),
		                                               PSource.PSpecified.DEFAULT);
		
		Cipher cipher = initCipher(rsaAesProperties.getRsaName(), mode, key, spec);
		return cryptToString(cipher, encoded, this::base64HexToBytes).split(SEPERATOR);
	}
	
	public String decryptMessage(byte[] aesKey, String text) {
		int mode = Cipher.DECRYPT_MODE;
		SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(AES_IV);
		
		Cipher cipher = initCipher(rsaAesProperties.getAesName(), mode, key, ivParameterSpec);
		return cryptToString(cipher, text, this::base64HexToBytes);
	}
	
	public String decryptAES(String text) {
		return decryptMessage(AES_SECRET_KEY, text);
	}
	
	private byte[] base64HexToBytes(String base64Hex) {
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
	
	private String bytesToBase64UrlHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return Base64.getUrlEncoder()
		             .withoutPadding()
		             .encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public Map<String, String> getEncodedPropsMap() {
		byte[] n = RSA_PUBLIC_KEY.getModulus().toByteArray();
		byte[] e = RSA_PUBLIC_KEY.getPublicExponent().toByteArray();
		return Map.of("n",
		              bytesToBase64UrlHex(n),
		              "e",
		              Base64.getEncoder().encodeToString(e),
		              "ak",
		              bytesToBase64Hex(AES_SECRET_KEY),
		              "iv",
		              bytesToBase64Hex(AES_IV),
		              "sep",
		              SEPERATOR);
	}
	
	public String getSeperator() {
		return SEPERATOR;
	}
	
	public String getSessionKey() {
		return rsaAesProperties.getSessionKey();
	}
	
}
