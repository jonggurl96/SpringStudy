package com.demo.spring.config.security.util.mng;


import com.demo.spring.config.security.exception.dec.CryptoException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.function.Function;

/**
 * CryptoManager.java
 * <pre>
 * 암/복호화 매니저
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 8. 29.
 */
@Slf4j
public abstract class CryptoManager {
	
	public abstract String decrypt(String text) throws CryptoException;
	
	/**
	 * @param algorithmFullName ${Algorithm Type}/${Operation Mode}/${Padding}
	 *
	 * @return Algorithm Instance
	 *
	 * @throws CryptoException
	 */
	public Cipher createCipher(String algorithmFullName) throws CryptoException {
		try {
			return Cipher.getInstance(algorithmFullName);
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new CryptoException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		}
	}
	
	/**
	 * @param algorithmFullName ${Algorithm Type}/${Operation Mode}/${Padding}
	 * @param mode              1: encrypt, 2: decrypt
	 * @param key
	 *
	 * @return
	 *
	 * @throws CryptoException
	 * @see #initCipher(String, int, Key, AlgorithmParameterSpec)
	 */
	public Cipher initCipher(String algorithmFullName, int mode, Key key) throws CryptoException {
		try {
			Cipher cipher = createCipher(algorithmFullName);
			cipher.init(mode, key);
			return cipher;
		} catch(InvalidKeyException ike) {
			throw new CryptoException("사용할 수 없는 키입니다.");
		}
	}
	
	/**
	 * @param algorithmFullName ${Algorithm Type}/${Operation Mode}/${Padding}
	 * @param mode              1: encrypt, 2: decrypt
	 * @param key
	 * @param spec
	 *
	 * @return
	 *
	 * @throws CryptoException
	 * @see #initCipher(String, int, Key)
	 */
	public Cipher initCipher(String algorithmFullName, int mode, Key key, AlgorithmParameterSpec spec) throws CryptoException {
		try {
			Cipher cipher = createCipher(algorithmFullName);
			cipher.init(mode, key, spec);
			return cipher;
		} catch(InvalidKeyException ike) {
			throw new CryptoException("사용할 수 없는 키입니다.");
		} catch(InvalidAlgorithmParameterException iape) {
			throw new CryptoException("알고리즘 ParameterSpec이 잘못되었습니다.", iape);
		}
	}
	
	/**
	 * @param cipher
	 * @param message    전처리 전 String
	 * @param preprocess lambda function (String) -> byte[]
	 *
	 * @return encrypted/decrypted String to byte[]
	 *
	 * @throws CryptoException
	 * @see #initCipher(String, int, Key)
	 */
	public byte[] crypt(Cipher cipher,
	                    String message,
	                    Function<String, byte[]> preprocess) throws CryptoException {
		byte[] bytes = preprocess.apply(message);
		return cryptEncoded(cipher, bytes);
	}
	
	/**
	 * @param cipher
	 * @param message     전처리 전 String
	 * @param preprocess  lambda (String) -> byte[]
	 * @param postprocess lambda(byte[]) -> T
	 * @param <T>
	 *
	 * @return encrypted/decrypted String to T
	 *
	 * @see #initCipher(String, int, Key)
	 */
	public <T> T cryptPostProcess(Cipher cipher,
	                              String message,
	                              Function<String, byte[]> preprocess,
	                              Function<byte[], T> postprocess) {
		return postprocess.apply(crypt(cipher, message, preprocess));
	}
	
	/**
	 * @param cipher
	 * @param message    전처리 전 String
	 * @param preprocess lambda (String) -> byte[]
	 *
	 * @return encrypted/decrypted String to UTF-8 String
	 *
	 * @see #initCipher(String, int, Key)
	 */
	public String cryptToString(Cipher cipher,
	                            String message,
	                            Function<String, byte[]> preprocess) {
		return cryptPostProcess(cipher, message, preprocess, (b) -> new String(b, StandardCharsets.UTF_8));
	}
	
	/**
	 * @param cipher
	 * @param bytes
	 *
	 * @return 암/복호화
	 *
	 * @throws CryptoException
	 * @see #initCipher(String, int, Key)
	 */
	public byte[] cryptEncoded(Cipher cipher, byte[] bytes) throws CryptoException {
		try {
			return cipher.doFinal(bytes);
		} catch(IllegalBlockSizeException ibe) {
			throw new CryptoException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new CryptoException("Padding이 잘못되었습니다.", bpe);
		}
	}
	
	/**
	 * @param cipher
	 * @param bytes       전처리 후 message
	 * @param postprocess lambda (byte[]) -> T
	 * @param <T>
	 *
	 * @return encrypted/decrypted byte[] to T
	 *
	 * @see #initCipher(String, int, Key)
	 */
	public <T> T cryptEncodedPostProcess(Cipher cipher,
	                                     byte[] bytes,
	                                     Function<byte[], T> postprocess) {
		return postprocess.apply(cryptEncoded(cipher, bytes));
	}
	
	/**
	 * @param cipher
	 * @param bytes  전처리 후 message
	 *
	 * @return encrypted/decrypted byte[] to UTF-8 String
	 *
	 * @see #initCipher(String, int, Key)
	 */
	public String cryptEncodedToString(Cipher cipher,
	                                   byte[] bytes) {
		return cryptEncodedPostProcess(cipher, bytes, (b) -> new String(b, StandardCharsets.UTF_8));
	}
	
	public abstract Map<String, String> getEncodedPropsMap();
	
}
