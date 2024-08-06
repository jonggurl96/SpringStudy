import NodeRSA from 'node-rsa';
import crypto from "crypto";

const aesAlgorithmName = 'aes-256-cbc';

/**
 * <pre>
 *     1. AES key값 RSA 복호화
 *     2. text AES 암호화
 *     3. 암호화 결과 반환
 * </pre>
 * @param rsaOptions
 * @param aesOptions
 */
function hybridEncrypt(rsaOptions, aesOptions) {
	const SECRET_KEY = Buffer.from(aesOptions.key);
	const INITIAL_VECTOR = Buffer.from(aesOptions.iv);

	const rsa = new NodeRSA({ b: rsaOptions.keySizeBit });

	const mod = rsaOptions.modulus;
	const exp = rsaOptions.exponent;
	rsa.keyPair.setPublic(mod, exp);

	console.log(`mod: ${mod}, exp: ${exp}, key: ${SECRET_KEY}, iv: ${INITIAL_VECTOR}`)

	const encryptedKey = Buffer.from(rsa.decrypt(SECRET_KEY, 'base64'))

	const cipher = crypto.createCipheriv(aesAlgorithmName, encryptedKey, INITIAL_VECTOR);

	return cipher.update(aesOptions.text, 'utf8', 'base64') + cipher.final('base64');

}

(function(window) {
	window.encryption = hybridEncrypt;
})(window);