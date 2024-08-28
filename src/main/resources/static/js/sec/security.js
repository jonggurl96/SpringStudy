async function createAesKey() {
	return await crypto.subtle.generateKey({
		name: 'AES-CBC',
		length: 256
	}, true, ['encrypt', 'decrypt']);
}

async function importRsaKey() {
	const n = atob(document.querySelector("#rsa-public-modulus").value);
	return await crypto.subtle.importKey("jwk",
		{
			kty: "RSA",
			e: document.querySelector("#rsa-public-exponent").value,
			n: n.startsWith("00") ? n.substring(2) : n,
			alg: "RSA-OAEP-256",
			ext: true
		},
		{ name: "RSA-OAEP", hash: { name: 'SHA-256' } },
		false,
		['encrypt']);
}

function decode(parameter) {
	const bytes = new Uint8Array(parameter);
	const byteStrArr = [];
	bytes.forEach(b => {
		const s = b.toString(16);
		byteStrArr.push(s.length === 1 ? "0" + s : s);
	});

	return btoa(byteStrArr.join(""));
}

async function rsaes(message = "") {
	const aesKey = await createAesKey();
	const rsaKey = await importRsaKey();
	const aesIv = crypto.getRandomValues(new Uint8Array(16));

	const keyBytes = await crypto.subtle.exportKey("raw", aesKey);

	const encryptedAesKey = await crypto.subtle.encrypt({
		name: "RSA-OAEP"
	}, rsaKey, new Uint8Array(keyBytes));

	const encrypted = await crypto.subtle.encrypt({
		name: 'AES-CBC',
		iv: aesIv
	}, aesKey, new TextEncoder().encode(message));

	return {
		aesKey: decode(encryptedAesKey),
		aesIv: decode(aesIv),
		encrypted: decode(encrypted),
		encryptedAesKey
	};
}

(function(window) {
	window.encrypt = rsaes;
})(window);