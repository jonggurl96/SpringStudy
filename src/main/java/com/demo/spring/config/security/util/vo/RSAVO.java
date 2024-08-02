package com.demo.spring.config.security.util.vo;


import com.demo.spring.config.security.exception.dec.DecryptException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
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
public record RSAVO(PrivateKey privateKey, String modulus, String exponent) implements DecoderVO {
	
	public static final String ALGORITHM = "RSA";
	
	public static final String ALGORITHM_FULL = "RSA/ECB/OAEPPadding";
	
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
		
		return new RSAVO(privateKey, modulus, exponent);
	}
	
	@Override
	public String decrypt(String encrypted) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_FULL);
			OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec("SHA-256", "MGF1",
			                                                            new MGF1ParameterSpec("SHA-256"),
			                                                            PSource.PSpecified.DEFAULT);
			
			cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParameterSpec);
			
			log.debug(">>> Algorithm. {}", cipher.getAlgorithm());
			log.debug(">>> Provider. {}", cipher.getProvider().toString());
			log.debug(">>> Parameters. {}", cipher.getParameters());
			
			
			
			byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new DecryptException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		} catch(InvalidKeyException ike) {
			throw new DecryptException("사용할 수 없는 키입니다.");
		} catch(IllegalBlockSizeException ibe) {
			throw new DecryptException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new DecryptException("Padding이 잘못되었습니다.", bpe);
		} catch(InvalidAlgorithmParameterException iape) {
			throw new DecryptException("ParameterSpec이 잘못되었습니다.", iape);
		}
	}
	
}
