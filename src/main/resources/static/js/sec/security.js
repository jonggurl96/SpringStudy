import NodeRSA from 'node-rsa';
import crypto from "crypto";

const aesAlgorithmName = 'aes-256-cbc';

/**
 * <pre>
 *     1. AES 키 생성
 *     2. 패스워드 AES 암호화
 *     3. RSA 공개키 생성
 *     4. AES 키 암호화
 *     5. 패스워드 값 변경: [암호화한 패스워드]|_|_|[암호화한 AES 비밀키]
 * </pre>
 * @param rsaOptions
 * @param aesOptions
 */
function hybridEncrypt(rsaOptions, aesOptions) {
	const SECRET_KEY = crypto.randomBytes(aesOptions.keySizeByte);
	const INITIAL_VECTOR = Buffer.from(aesOptions.iv, 'base64');

	console.log(INITIAL_VECTOR)

	const cipher = crypto.createCipheriv(aesAlgorithmName, SECRET_KEY, INITIAL_VECTOR);

	const encrypted64 = cipher.update(aesOptions.text, 'utf8', 'base64') + cipher.final('base64');

	const rsa = new NodeRSA({ b: rsaOptions.keySizeBit });
	rsa.keyPair.setPublic(atob(rsaOptions.modulus), atob(rsaOptions.exponent));
	const encryptedSecret = rsa.encrypt(SECRET_KEY, 'base64', 'buffer');

	return `${encrypted64}--==AES==--${encryptedSecret}`;
}

(function(window) {
	window.encryption = hybridEncrypt;
})(window);