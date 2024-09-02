package com.demo.spring.config.security.encoder;


import com.demo.spring.config.security.exception.dec.CryptoException;
import com.demo.spring.config.security.util.properties.RsaAesProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class AesEncoder implements PasswordEncoder {
	
	private final String aesKey;
	
	private final String aesName;
	
	private final int ivSize;
	
	public AesEncoder(RsaAesProperties properties) {
		this.aesKey = properties.getAesKey();
		this.ivSize = properties.getAesKeySize() / 2;
		this.aesName = properties.getAesName();
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		byte[] aesKey = base64HexToBytes(this.aesKey);
		
		byte[] iv = new byte[ivSize];
		
		System.arraycopy(aesKey, 0, iv, 0, ivSize);
		
		SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		
		try {
			Cipher cipher = Cipher.getInstance(this.aesName);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
			byte[] encrypted = cipher.doFinal(((String) rawPassword).getBytes(StandardCharsets.UTF_8));
			return bytesToBase64(encrypted);
		} catch(NoSuchPaddingException | NoSuchAlgorithmException nse) {
			throw new CryptoException("알고리즘 정보를 불러오는 데 실패했습니다.", nse);
		} catch(InvalidKeyException ike) {
			throw new CryptoException("사용할 수 없는 키입니다.");
		} catch(InvalidAlgorithmParameterException iape) {
			throw new CryptoException("알고리즘 ParameterSpec이 잘못되었습니다.", iape);
		} catch(IllegalBlockSizeException ibe) {
			throw new CryptoException("Block Size가 잘못 지정되었습니다.", ibe);
		} catch(BadPaddingException bpe) {
			throw new CryptoException("Padding이 잘못되었습니다.", bpe);
		}
	}
	
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String encoded = encode(rawPassword);
		
		log.debug(">>> Encrypt rawPassword AES: {}", encoded);
		log.debug(">>> EncodedPassword: {}", encodedPassword);
		return encoded.equals(encodedPassword);
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
	
	private String bytesToBase64(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return Base64.getEncoder().encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
	}
	
}
