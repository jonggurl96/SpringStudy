import NodeRSA from 'node-rsa';

(function(window) {
	window.encrypt = (text, modulus, exponent) => {
		const rsa = new NodeRSA({ b: 2048 })
		rsa.keyPair.setPublic(atob(modulus), atob(exponent))
		return rsa.encrypt(text, 'utf8', 'utf8')
	}

})(window);