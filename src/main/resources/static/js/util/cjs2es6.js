(function(window) {
	window.require = (id) => {
		let CJS
		import(id).then(a => CJS = a)
		return CJS
	}
})(window);