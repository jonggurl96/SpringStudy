package com.demo.spring.config.security.util.vo;


import com.demo.spring.config.security.exception.dec.DecryptException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
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
public record RSAVO(PrivateKey privateKey, PublicKey publicKey, String modulus, String exponent,
                    String priExp) implements CryptoVO {
	
	public static final String ALGORITHM = "RSA";
	
	public static final String ALGORITHM_FULL = "RSA/ECB/PKCS1Padding";
	
	public String base64Modulus() {
		return base64EncodeToString(modulus.getBytes());
	}
	
	public String base64Exponent() {
		return base64EncodeToString(exponent.getBytes());
	}
	
	public String base64PriExp() {
		return base64EncodeToString(priExp.getBytes());
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
		
		String priExp = ((RSAPrivateKey) privateKey).getPrivateExponent().toString(modulusRadix);
		
		return new RSAVO(privateKey, publicKey, modulus, exponent, priExp);
	}
	
	@Override
	public String crypt(int cryptMode, String text) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
			
			Key key = cryptMode == Cipher.ENCRYPT_MODE ? privateKey : publicKey;
			
			cipher.init(cryptMode, key);
			
			byte[] bytes = cryptMode == Cipher.DECRYPT_MODE ? base64Decode(text)
			                                                : text.getBytes(StandardCharsets.UTF_8);
			
			byte[] decrypted = cipher.doFinal(bytes);
			
			return cryptMode == Cipher.ENCRYPT_MODE ? base64EncodeToString(decrypted)
			                                        : new String(decrypted, StandardCharsets.UTF_8);
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new DecryptException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		} catch(InvalidKeyException ike) {
			throw new DecryptException("사용할 수 없는 키입니다.");
		} catch(IllegalBlockSizeException ibe) {
			throw new DecryptException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new DecryptException("Padding이 잘못되었습니다.", bpe);
		}
	}
	
}
