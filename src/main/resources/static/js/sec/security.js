import NodeRSA from 'node-rsa';
import crypto from "crypto";

const aesAlgorithmName = 'aes-256-cbc';

function getRsa(bitsize, modulus, exponent) {
	const rsa = new NodeRSA({ b: bitsize });
	rsa.keyPair.setPublic(atob(modulus), atob(exponent));
	return rsa;
}

function base64(selector) {
	return atob(document.querySelector(selector).value);
}

function encryptWithRsa(text, modulus, exponent) {
	return getRsa(2048, modulus, exponent).encrypt(text, 'hex', 'utf8');
}

function decryptWithRsa(text, modulus, exponent) {
	return getRsa(2048, modulus, exponent).decrypt(text, 'utf8');
}

function decryptWithAes(text, decodedKey) {
	const salt = base64("#aes-salt");

	// 더 강력한 암호화를 위해 사용하는 초기화 벡터
	const iv = base64("#aes-iv");

	const scryptedKey = crypto.scryptSync(decodedKey, salt, 32);
	const cipher = crypto.createCipheriv(aesAlgorithmName, scryptedKey, iv);

	let result = cipher.update(text, 'utf8', 'base64');
	result += cipher.final('base64');
	return result;
}

function hybridEncrypt(text, modulus, exponent) {
	const key = base64("#aes-secret-key");
	const decodedKey = decryptWithRsa(key, modulus, exponent);
	return decryptWithAes(text, decodedKey);
}

(function(window) {

	window.rsaEncrypt = encryptWithRsa;

	window.rsaDecrypt = decryptWithRsa;

	window.aesDecrypt = decryptWithAes;

	window.encryption = hybridEncrypt;
})(window);