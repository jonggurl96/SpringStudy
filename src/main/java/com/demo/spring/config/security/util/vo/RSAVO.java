package com.demo.spring.config.security.util.vo;


import com.demo.spring.config.security.exception.dec.DecryptException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

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
	
	public static final String ALGORITHM_FULL = "RSA/ECB/PKCS1Padding";
	
	
	public static RSAVO generate(int keySize) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(keySize, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		return new RSAVO(privateKey, publicKey);
	}
	
	@Override
	public String crypt(int cryptMode, String text) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
			
			Key key = cryptMode == Cipher.ENCRYPT_MODE ? privateKey : publicKey;
			
			cipher.init(cryptMode, key);
			
			byte[] bytes = getCryptBytes(cryptMode, text);
			
			byte[] decrypted = cipher.doFinal(bytes);
			
			return getCryptResult(cryptMode, decrypted);
			
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
