async function importAesKey() {
	return await crypto.subtle.importKey("raw",
		encodeAesKey(), {
			name:   'AES-CBC',
			length: 256
		}, false, ['encrypt']);
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

function base64UrlHexToBytes(base64UrlHex = "") {
	const hexString = atob(base64UrlHex);
	const bytes = [];
	
	for(let i = 0; i < hexString.length; i += 2) {
		bytes.push(parseInt(hexString.substring(i, i + 2), 16));
	}
	
	return bytes;
}

function importModulus() {
	const modBytes = base64UrlHexToBytes(document.querySelector("#rsa-public-modulus").value);
	// TODO 0번방 제거
	
	return btoa(modBytes.map(b => String.fromCharCode(b)).join(""))
		.replaceAll(/\+/g, "-")
		.replaceAll(/\//g, "_")
		.replaceAll(/=/g, "");
}

function encodeIv() {
	const bytes = base64UrlHexToBytes(document.querySelector("#aes-iv").value);
	return new Uint8Array(bytes);
}

function encodeAesKey() {
	const bytes = base64UrlHexToBytes(document.querySelector("#aes-key").value);
	return new Uint8Array(bytes);
}

function toBase64Hex(parameter) {
	const bytes = new Uint8Array(parameter);
	const byteStrArr = [];
	bytes.forEach(b => {
		if(b < 16) byteStrArr.push("0");
		byteStrArr.push(b.toString(16));
	});
	
	return btoa(byteStrArr.join(""));
}

async function rsaes(message = "") {
	const aesKey = await importAesKey();
	const rsaKey = await importRsaKey();
	const aesIv = encodeIv();
	
	const encrypted = await crypto.subtle.encrypt({
		name: 'AES-CBC',
		iv:   aesIv
	}, aesKey, new TextEncoder().encode(message));
	
	const aeskey64url = document.querySelector("#aes-key").value;
	const crypto = toBase64Hex(encrypted);
	const aesIv64Url = document.querySelector("#aes-iv").value;
	const sep = document.querySelector("#rsaes-sep").value;
	
	const willbeRSA = `${aeskey64url}${sep}${crypto}${sep}${aesIv64Url}`;
	
	const cryptoRSA = await crypto.subtle.encrypt({
		name: "RSA-OAEP"
	}, rsaKey, new TextEncoder().encode(willbeRSA));
	
	return toBase64Hex(cryptoRSA);
}

(function(window) {
	window.encrypt = rsaes;
})(window);