window.onload = () => {
	const errMsg = document.querySelector("#errMsg").value;
	if(errMsg)
		alert(errMsg);

	document.querySelector("#loginBtn").addEventListener("click", () => {
		const elPassword = document.loginForm.password;
		const elKey = document.loginForm.encryptedKey;
		const elIv = document.loginForm.iv;

		encrypt(elPassword.value).then(({ aesKey, aesIv, encrypted, keyBytes, encryptedAesKey }) => {
			console.log("<<encryptedAesKey>>");
			console.log(new Uint8Array(encryptedAesKey));

			console.log("<<modulus>>");
			const n = atob(document.querySelector("#rsa-public-modulus").value);
			const nn = n.startsWith("00") ? n.substring(2) : n;
			console.log(nn);
			console.log(nn.length);
			// alert(encrypted);
			// elPassword.value = encrypted;
			// elKey.value = aesKey;
			// elIv.value = aesIv;
			// document.loginForm.submit();
		});

	});
};

function getContextPath() {
	let contextPath = null;
	if(contextPath === null) return null;

	if(!contextPath.startsWith("/"))
		contextPath = "/" + contextPath;

	if(!contextPath.endsWith("/"))
		contextPath += "/";
}

/**
 *
 * @param form {HTMLFormElement}
 * @returns {string}
 */
function formToQueryString(form) {
	const params = form.querySelectorAll("input[name], select[name]");
	return Array.from(params.values())
		.filter(element => element.value !== null)
		.map(element => `${element.name}=${element.value}`)
		.join("&");
}

/**
 *
 * @param form {HTMLFormElement}
 * @param options {Object}
 * @returns {Promise<Response>}
 */
function postForm(form, options) {
	return post(form.action, formToQueryString(form), options);
}

/**
 *
 * @param url {string}
 * @param data {string}
 * @param options {Object}
 * @returns {Promise<Response>}
 */
function post(url, data, options) {
	const fetchOptions = Object.assign({
		method: "POST", // *GET, POST, PUT, DELETE 등
		mode: "cors", // no-cors, *cors, same-origin
		cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
		credentials: "same-origin", // include, *same-origin, omit
		headers: {
			// "Content-Type": "application/json",
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		redirect: "follow", // manual, *follow, error
		referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
		body: data,
	}, options);

	const contextPath = getContextPath();
	url = contextPath === null ? url : contextPath + url;

	return fetch(url, fetchOptions);
}