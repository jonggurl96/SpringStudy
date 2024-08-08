import * as crypto from "node:crypto";

function seperateParamNames(encoded) {
	const arr = atob(encoded).split("[|<|NAMES|>|]");
	const paramNameArr = arr[0].split("|");
	const params = {
		prfx: paramNameArr[0],
		rk: paramNameArr[1],
		sfx: paramNameArr[2],
		ak: paramNameArr[3],
		iv: paramNameArr[4]
	};

	const paramObj = JSON.parse(arr[1]) || {};
	Object.keys(params).forEach(k => {
		params[k] = paramObj[params[k]];
	});

	return params;
}

function decryptAesKey(params) {
	const { prfx, rk, sfx, ak } = params;
	const privateKeyData = [prfx, rk, sfx].join("\n");
	const privateKey = crypto.createPrivateKey(privateKeyData);
	return crypto.privateDecrypt(privateKey, Buffer.from(ak)).toString("utf8");
}

function encrypt(text, params) {
	const { key, slt, iv } = params;

	const saltedKey = crypto.scryptSync(Buffer.from(key), Buffer.from(slt), 32);
	const cipher = crypto.createCipheriv("aes-256-cbc", saltedKey, iv);

	return cipher.update(text, "utf8", "base64") + cipher.final("base64");
}

function raEnc(text, encoded) {
	const params = seperateParamNames(encoded);
	params.ak = decryptAesKey(params);
	return encrypt(text, params);
}

(function(window) {
	window.encrypt = raEnc;
})(window);