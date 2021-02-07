/**
 * 封装http请求
 */
(function () {

	var networkConfig = $config.network;

	axios.defaults.timeout = networkConfig.connectTimeout;
	axios.defaults.baseURL = '';

	// ==================================== token 相关操作 ============================================================

	function setToken(token) {
		localStorage.setItem(networkConfig.tokenName, networkConfig.tokenStartStr + token);
		$cookies.set(networkConfig.cookieName, token);
	}

	function getToken() {
		return localStorage.getItem(networkConfig.tokenName);
	}

	function delToken() {
		localStorage.remove(networkConfig.tokenName);
		$cookies.remove(networkConfig.cookieName)
	}

	// ==================================== response状态码 相关操作 ===================================================

	function responseStatusHandle(status) {
		switch (status) {
			case 401: {
				delToken();
				window.parent.location.href = xh.base + "/login.html";
				break;
			}
			case 403: {
				delToken();
				window.parent.location.href = xh.base + "/403.html";
				break;
			}
			default: {

			}
		}
	}

	// ==================================== http request 拦截器 =======================================================
	axios.interceptors.request.use(
		function (config) {

			config.headers = {
				'Content-Type': 'application/x-www-form-urlencoded',
				'Cache-Control': 'no-cache',
				'Pragma': 'no-cache',
				'X-Requested-With': 'XMLHttpRequest'
			};
			if (config.method === 'post' && config.headers["Content-Type"] === "application/x-www-form-urlencoded") {
				config.data = Qs.stringify(config.data, {
					allowDots: true
				});
			}

			if (config.url.indexOf('/login') === -1) {
				var token = getToken();
				if (token) {
					config.headers[networkConfig.tokenHeaderName] = token;
				}
			}
			return config;
		},
		function (error) {
			return Promise.reject(error);
		}
	);

	// ==================================== http response 拦截器 ======================================================
	axios.interceptors.response.use(
		function (response) {
			if (response.data.token) {
				var token = response.data.token;
				setToken(token);
			}
			return response;
		},
		function (error) {
			responseStatusHandle(error.response.data.status);
			return Promise.reject(error)
		}
	);

	function ajax(conf) {
		if (conf !== undefined) {
			var _axios = axios.create({
				baseURL: conf.baseURL === undefined ? axios.defaults.baseURL : conf.baseURL,
				timeout: conf.timeout === undefined ? axios.defaults.timeout : conf.timeout,
				headers: conf.headers === undefined ? null : conf.headers
			});
			_axios.interceptors.request.use(
				function (config) {
					if (config.method === 'post' && config.headers["Content-Type"] === "application/x-www-form-urlencoded") {
						config.data = Qs.stringify(config.data, {
							allowDots: true
						});
					}
					return config;
				},
				function (error) {
					return Promise.reject(error);
				}
			);
			return _axios;
		}
		return axios;
	}


	/**
	 * 封装get方法
	 * @param url
	 * @param params
	 * @returns {Promise}
	 */

	function get(url, params) {
		if (params === undefined) {
			params = {}
		}
		return new Promise(function (resolve, reject) {
			ajax().get(url, {params: params})
				.then(function (response) {
					resolve(response.data);
				})
				.catch(function (err) {
					reject(err)
				})
		})
	}


	/**
	 * 封装post请求
	 * @param url
	 * @param data
	 * @param conf
	 * @returns {Promise}
	 */

	function post(url, data, conf) {

		if (data === undefined) {
			data = {}
		}

		return new Promise(function (resolve, reject) {
			ajax(conf).post(url, data, conf)
				.then(function (response) {
					resolve(response.data);
				}, function (err) {
					reject(err)
				})
		})
	}

	/**
	 * 封装patch请求
	 * @param url
	 * @param data
	 * @param conf
	 * @returns {Promise}
	 */

	function patch(url, data, conf) {

		if (data === undefined) {
			data = {}
		}
		return new Promise(function (resolve, reject) {
			ajax(conf).patch(url, data, conf)
				.then(function (response) {
					resolve(response);
				}, function (err) {
					reject(err)
				})
		})
	}

	/**
	 * 封装put请求
	 * @param url
	 * @param data
	 * @param conf
	 * @returns {Promise}
	 */
	function put(url, data, conf) {

		if (data === undefined) {
			data = {}
		}
		return new Promise(function (resolve, reject) {
			ajax(conf).put(url, data, conf)
				.then(function (response) {
					resolve(response.data);
				}, function (err) {
					reject(err)
				})
		})
	}


	var http = {
		get: get,
		post: post,
		put: put,
		patch: patch,
		setAjaxToken: function () {
			var token = getToken();
			var headers = {};
			headers[networkConfig.tokenHeaderName] = token;
			$.ajaxSetup({
				headers: headers,
				error: function (jqXHR) {
					responseStatusHandle(jqXHR.status);
				}
			});
		}
	};

	if (typeof xh !== "object") {
		window.xh = {};
	}
	window.xh.http = http;
	window.xh.isLoginRedirect = true;
}());
