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

async function decryptAesKey(params) {
	const { prfx, rk, sfx, ak } = params;
	const privateKeyData = Buffer.from([prfx, rk, sfx].join("\n"));
	const privateKey = await crypto.subtle.importKey("pkcs8", privateKeyData, {
		name: 'RSA-OAEP',
		hash: { name: 'SHA-512' }
	}, false, ["sign"]);
	return await crypto.subtle.decrypt({
		name: 'RSA-OAEP'
	}, privateKey, Buffer.from(ak));
}

async function encrypt(text, params) {
	const { ak, slt, iv } = params;

	const key = await crypto.subtle.importKey("raw", ak, { name: 'AES-CBC' }, false, ["encrypt"]);

	return await crypto.subtle.encrypt({ name: 'AES-CBC', iv: Buffer.from(iv, 'base64') }, key, Buffer.from(text));
}

async function raEnc(text, encoded) {
	const params = seperateParamNames(encoded);
	params.ak = await decryptAesKey(params);
	return await encrypt(text, params);
}

(function(window) {
	window.secEnc = raEnc;
})(window);