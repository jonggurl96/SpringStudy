async function createAesKey() {
	return await crypto.subtle.generateKey({
		name:   'AES-CBC',
		length: 256
	}, true, ['encrypt']);
}

async function importRsaKey() {
	return await crypto.subtle.importKey("jwk", {
		kty: "RSA",
		e:   document.querySelector("#rsa-public-exponent").value,
		n:   importModulus(),
		alg: "RSA-OAEP-256",
	}, {
		name: "RSA-OAEP",
		hash: { name: "SHA-256" }
	}, false, ['encrypt']);
}

function importModulus() {
	const encodedHexString = atob(document.querySelector("#rsa-public-modulus").value);
	const modBytes = [];
	
	for(let i = 2; i < encodedHexString.length; i += 2) {
		modBytes.push(parseInt(encodedHexString.substring(i, i + 2), 16));
	}
	
	return btoa(modBytes.map(b => String.fromCharCode(b)).join(""))
		.replaceAll(/\+/g, "-")
		.replaceAll(/\//g, "_")
		.replaceAll(/=/g, "");
}

function encodeIv() {
	const iv = atob(document.querySelector("#aes-iv").value);
	const bytes = [];
	
	for(let i = 0; i < iv.length; i += 2) {
		bytes.push(parseInt(iv.substring(i, i + 2), 16));
	}
	
	return new Uint8Array(bytes);
}

function decode(parameter) {
	const bytes = new Uint8Array(parameter);
	const byteStrArr = [];
	bytes.forEach(b => {
		if(b < 16) byteStrArr.push("0");
		byteStrArr.push(b.toString(16));
	});
	
	return btoa(byteStrArr.join(""));
}

async function rsaes(message = "") {
	const aesKey = await createAesKey();
	const rsaKey = await importRsaKey();
	
	const aesIv = encodeIv();
	
	const keyBytes = await crypto.subtle.exportKey("raw", aesKey);
	
	const encryptedAesKey = await crypto.subtle.encrypt({
		name: "RSA-OAEP"
	}, rsaKey, new Uint8Array(keyBytes));
	
	const encrypted = await crypto.subtle.encrypt({
		name: 'AES-CBC',
		iv:   aesIv
	}, aesKey, new TextEncoder().encode(message));
	
	
	return {
		aesKey:    decode(encryptedAesKey),
		aesIv:     decode(aesIv),
		encrypted: decode(encrypted)
	};
}

(function(window) {
	window.encrypt = rsaes;
})(window);