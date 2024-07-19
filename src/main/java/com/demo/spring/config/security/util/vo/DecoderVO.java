package com.demo.spring.config.security.util.vo;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public interface DecoderVO {
	
	public abstract PrivateKey privateKey();
	
	public abstract String base64Modulus();
	
	public abstract String base64Exponent();
	
	public abstract String decrypt(String encrypted) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;
	
}
