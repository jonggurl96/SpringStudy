/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./sec/security.js":
/*!*************************!*\
  !*** ./sec/security.js ***!
  \*************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

eval("__webpack_require__.r(__webpack_exports__);\nasync function createAesKey() {\r\n\treturn await crypto.subtle.generateKey({\r\n\t\tname: 'AES-CBC',\r\n\t\tlength: 256\r\n\t}, true, ['encrypt', 'decrypt']);\r\n}\r\n\r\nasync function importRsaKey() {\r\n\treturn await crypto.subtle.importKey(\"jwk\",\r\n\t\t{\r\n\t\t\tkty: \"RSA\",\r\n\t\t\te: btoa(document.querySelector(\"#rsa-public-exponent\").value),\r\n\t\t\tn: btoa(document.querySelector(\"#rsa-public-modulus\").value),\r\n\t\t\talg: \"RSA-OAEP-256\",\r\n\t\t\text: true\r\n\t\t},\r\n\t\t{ name: \"RSA-OAEP\", hash: { name: 'SHA-256' } },\r\n\t\tfalse,\r\n\t\t['encrypt']);\r\n}\r\n\r\nfunction decode(parameter) {\r\n\tconst ec = new TextDecoder('utf8');\r\n\treturn btoa(ec.decode(parameter));\r\n}\r\n\r\nasync function rsaes(message = \"\") {\r\n\tconst aesKey = await createAesKey();\r\n\tconst rsaKey = await importRsaKey();\r\n\tconst aesIv = crypto.getRandomValues(new Uint8Array(16));\r\n\r\n\tconst keyBytes = await crypto.subtle.exportKey(\"pkcs8\", aesKey);\r\n\r\n\tconst encryptedAesKey = await crypto.subtle.encrypt({\r\n\t\tname: \"RSA-OAEP\"\r\n\t}, rsaKey, keyBytes);\r\n\r\n\tconst encrypted = await crypto.subtle.encrypt({\r\n\t\tname: 'AES-CBC',\r\n\t\tiv: aesIv\r\n\t}, aesKey, new TextEncoder().encode(message));\r\n\r\n\treturn {\r\n\t\taesKey: decode(encryptedAesKey),\r\n\t\taesIv: decode(aesIv),\r\n\t\tencrypted: decode(encrypted)\r\n\t};\r\n}\r\n\r\n(function(window) {\r\n\twindow.encrypt = rsaes;\r\n})(window);\n\n//# sourceURL=webpack://js/./sec/security.js?");

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The require scope
/******/ 	var __webpack_require__ = {};
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module can't be inlined because the eval devtool is used.
/******/ 	var __webpack_exports__ = {};
/******/ 	__webpack_modules__["./sec/security.js"](0, __webpack_exports__, __webpack_require__);
/******/ 	
/******/ })()
;