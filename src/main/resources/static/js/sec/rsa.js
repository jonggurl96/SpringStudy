import * as NodeRSA from 'node-rsa'

// function encrypt(text, modulus, exponent) {
// 	const rsa = new NodeRSA()
// 	rsa.keyPair.setPublic(atob(modulus), atob(exponent))
// 	const isPrivate = false
// 	return rsa.keyPair.encrypt(text, isPrivate)
// }

(function(window) {
	console.log(window)
	window.encrypt = (text, modulus, exponent) => {
		const rsa = new NodeRSA()
		rsa.keyPair.setPublic(atob(modulus), atob(exponent))
		const isPrivate = false
		return rsa.keyPair.encrypt(text, isPrivate)
	}
})(window)