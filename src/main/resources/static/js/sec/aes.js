import * as crypto from "crypto";

(function(window) {
	const algorithm = 'aes-256-cbc';

	// password, salt, byte로 생성된 키
	const key = crypto.scryptSync('wolfootjaIsSpecial', 'specialSalt', 32);

	// 더 강력한 암호화를 위해 사용하는 초기화 벡터
	const iv = crypto.randomBytes(16);

	const cipher = crypto.createCipheriv(algorithm, key, iv); //key는 32바이트, iv는 16바이트

	window.aes = (text) => {
		let result = cipher.update(text, 'utf8', 'base64');
		result += cipher.final('base64');
		console.log('암호화: ', result);
	}

})(window);
