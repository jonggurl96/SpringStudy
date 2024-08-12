import { resolve } from 'path'
import webpack from "webpack";

const __dirname = import.meta.dirname

export default {
	entry: "./sec/security.js",
	output: {
		path: resolve(__dirname, "dist"),
		filename: "index.js"
	},
	mode: "development",
	resolve: {
		alias: {
			'node:crypto': "crypto-browserify",
			stream: "stream-browserify",
			vm: "vm-browserify",
		},
	},
	plugins: [
		new webpack.ProvidePlugin({
			process: 'process/browser.js',
			Buffer: ['buffer', 'Buffer']
		})
	]
}