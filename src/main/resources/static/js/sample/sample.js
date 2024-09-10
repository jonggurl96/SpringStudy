function clickA() {
	restText("/api/a/text");
}

function clickB() {
	restText("/api/b/text");
}

function restText(url = "") {
	post(url).then(async (response) => alert(await response.text()));
}
