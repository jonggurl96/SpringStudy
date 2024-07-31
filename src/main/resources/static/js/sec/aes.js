import * as crypto from "crypto";

(function(window) {
	const algorithm = 'aes-256-cbc';
	const salt = atob(document.querySelector("#aes-salt").value);

	// 더 강력한 암호화를 위해 사용하는 초기화 벡터
	const iv = atob(document.querySelector("#aes-iv").value)

	window.aes = (text, decodedKey) => {
		const scryptedKey = crypto.scryptSync(decodedKey, salt, 32);
		const cipher = crypto.createCipheriv(algorithm, scryptedKey, iv);
		let result = cipher.update(text, 'utf8', 'base64');
		result += cipher.final('base64');
		console.log('암호화: ', result);
	}

})(window);
