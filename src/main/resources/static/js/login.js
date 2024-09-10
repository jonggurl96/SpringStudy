window.onload = () => {
	const errMsg = document.querySelector("#errMsg").value;
	if(errMsg)
		alert(errMsg);

	document.querySelector("#loginBtn").addEventListener("click", async () => {
		const elPassword = document.loginForm.password;

		encrypt(elPassword.value).then(crypto => {
			elPassword.value = crypto;
			document.loginForm.submit();
		});

	});
};
