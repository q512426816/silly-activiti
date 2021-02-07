/**
 * 基础全局配置
 */
(function () {
	var network = {
		domain: 'http://127.0.0.1',
		contentPath: '/xh-admin',
		tokenStartStr: 'Bearer ',
		tokenHeaderName: 'Authorization',
		tokenName: 'token',
		cookieName: 'crrcdt_token',
		connectTimeout: 6000
	};

	if (!window.$config) {
		window.$config = {
			network: network
		};
	}
})();