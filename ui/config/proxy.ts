/**
 * 代理的配置.
 * @see 注意：在生产环境，代理是无法生效的
 * -------------------------------
 * The agent cannot take effect in the production environment
 * so there is no configuration of the production environment
 * For details, please see
 * https://pro.ant.design/docs/deploy
 * @doc https://umijs.org/docs/guides/proxy
 * @doc https://github.com/chimurai/http-proxy-middleware
 */
export default {
	dev: {
		'/api/': {
			// 要代理的地址
			target: 'https://laokou.org:5555',
			changeOrigin: true,
			secure: false,
			pathRewrite: {'^/api': ''},
		},
	},
};
