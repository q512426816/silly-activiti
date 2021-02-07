<html>
<head>
	<meta charset="utf-8"/>
	<title>新航培训</title>
	<#include 'header.ftl'/>

	<link rel="stylesheet" type="text/css"
	      href="${request.contextPath}/statics/plugins/TextInputEffects/css/normalize.css"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/statics/plugins/TextInputEffects/css/set1.css"/>
	<style>
		[v-cloak] {
			display: none;
		}
	</style>
</head>
<body class="custom-body">
<div id="app" v-cloak>
	<!--大容器开始-->
	<div class="class-1" @keydown.13='login'>
		<!--大容器开始-->
		<div class="class-2">
			<!--大容器开始-->
			<div class="class-3">
				<!--图片开始-->
				<img src="${request.contextPath}/statics/images/main.jpg"
				     class="class-4"/>
				<!--图片结束-->
			</div>
			<!--大容器结束-->
			<!--大容器开始-->
			<div class="class-5">
				<!--小容器开始-->
				<div class="class-6">
					<!--文本开始-->
					<div class="class-7">
						<img src="${request.contextPath}/statics/images/log1.png" height="50"/>
					</div>
					<!--文本结束-->
					<!--小容器开始-->
					<el-form :model="form" ref="form" :rules="rules">
						<div class="class-8">
							<el-form-item prop="managerName">
                     <span class="input input--hoshi">
                          <input v-model="form.username" class="input__field input__field--hoshi" type="text"
                                 id="input-name" autocomplete="off"/>
                          <label class="input__label input__label--hoshi input__label--hoshi-color-1" for="input-name">
                              <span class="input__label-content input__label-content--hoshi">账号</span>
                          </label>
                      </span>
							</el-form-item>
						</div>
						<!--小容器结束-->
						<!--小容器开始-->
						<div class="class-13">
							<!--文本开始-->
							<el-form-item prop="managerPassword">
                  <span class="input input--hoshi">
                          <input v-model="form.password" class="input__field input__field--hoshi" type="password"
                                 id="input-password" autocomplete="off"/>
                          <label class="input__label input__label--hoshi input__label--hoshi-color-1"
                                 for="input-password">
                              <span class="input__label-content input__label-content--hoshi">密码</span>
                          </label>
                      </span>
							</el-form-item>
						</div>
						<!--小容器结束-->
						<!--小容器开始-->
						<div class="class-16">
							<!--小容器开始-->
							<div class="class-17">
								<!--文本开始-->
								<el-form-item prop="rand_code">
									 <span class="input input--hoshi">
										  <input v-model="form.captcha" class="input__field input__field--hoshi" type="text"
												 id="input-rand-code" autocomplete="off"/>
										  <label class="input__label input__label--hoshi input__label--hoshi-color-1"
												 for="input-rand-code">
											  <span class="input__label-content input__label-content--hoshi">验证码</span>
										  </label>
									  </span>
								</el-form-item>
							</div>
							<!--小容器结束-->
							<!--大容器开始-->
							<div class="class-20">
								<img :src="'xh.base'+captcha" class="code-img" @click="code"/>
							</div>
							<!--大容器结束-->
							<!--小容器开始-->
							<div class="class-21">
								<!--小容器开始-->
								<div @click="code"
								     class="class-22">
									<!--文本开始-->
									<div class="class-23">看不清？</div>
									<!--文本结束-->
									<!--文本开始-->
									<div class="class-24">换一张</div>
									<!--文本结束-->
								</div>
								<!--小容器结束-->
							</div>
							<!--小容器结束-->
						</div>
						<!--小容器结束-->
					</el-form>
					<!--小容器开始-->
					<div class="class-25">
					</div>
					<!--小容器结束-->
					<!--按钮开始-->
					<el-button @click="login" type="primary" :loading="loading"
					           class="class-26">
						{{loading?'登录中':'登录'}}
					</el-button>
					<!--按钮结束-->
					<!--小容器开始-->
					<div class="class-27">
						<el-checkbox v-model="rememberPass">记住密码</el-checkbox>
						<!--文本结束-->
					</div>
					<!--小容器结束-->
				</div>
				<!--小容器结束-->
			</div>
			<!--大容器结束-->
		</div>
		<!--大容器结束-->
	</div>
	<!--大容器结束-->
</div>
</body>
</html>
<script src="${request.contextPath}/statics/plugins/TextInputEffects/js/classie.js"></script>
<script>
	var app = new Vue({
		el: '#app',
		watch: {},
		data: {
			base: baseURL,
			loading: false,
			form: {
				username: '',
				password: '',
				captcha: ''
			},
			rules: {
				username: [
					{required: true, message: '请输入账号', trigger: 'blur'},
					{min: 1, max: 30, message: '长度不能超过30个字符', trigger: 'change'}
				],
				password: [
					{required: true, message: '请输入密码', trigger: 'blur'},
					{min: 1, max: 30, message: '长度不能超过30个字符', trigger: 'change'}
				],
				captcha: [
					{required: true, message: '请输入验证码', trigger: 'blur'},
					{min: 4, max: 4, message: '长度4个字符', trigger: 'change'}
				]
			},
			rememberPass: '',
			captcha: "captcha.jpg?t=" + $.now()
		},
		methods: {
			//登录
			login: function () {
				var that = this;
				that.$refs.form.validate(function (valid) {
					if (valid) {
						that.loading = true;
						xh.http.post(xh.base + "/login", {
							username: that.form.username,
							password: that.form.password,
							captcha: that.form.captcha
						}).then(function (res) {
							that.rememberPass ? localStorage.setItem("username", that.form.username) : localStorage.setItem('username', "");
							that.rememberPass ? localStorage.setItem('password', that.form.password) : localStorage.setItem('password', "");

							location.href = 'index.crrcdt';
						}).catch(function (err) {
							that.loading = false;
							that.$notify({
								title: '失败',
								message: "系统错误",
								type: 'error'
							});
							console.log(err);
						});
					}
				});
			},
			//获取验证码
			code: function () {
				this.captcha = "captcha.jpg?t=" + $.now();
			},
			//初始
			initial: function () {
				this.form.username = localStorage.getItem('username');
				this.form.password = localStorage.getItem('password');
				top.location !== self.location ? (top.location = self.location) : '';
			}
		},
		created: function () {
			this.code();
			this.initial();
		}
	})
</script>
<script>
	(function () {
		// trim polyfill : https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/Trim
		if (!String.prototype.trim) {
			(function () {
				// Make sure we trim BOM and NBSP
				var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
				String.prototype.trim = function () {
					return this.replace(rtrim, '');
				};
			})();
		}

		[].slice.call(document.querySelectorAll('input.input__field')).forEach(function (inputEl) {
			// in case the input is already filled..
			if (inputEl.value.trim() !== '') {
				classie.add(inputEl.parentNode, 'input--filled');
			}

			// events:
			inputEl.addEventListener('focus', onInputFocus);
			inputEl.addEventListener('blur', onInputBlur);
		});

		function onInputFocus(ev) {
			classie.add(ev.target.parentNode, 'input--filled');
		}

		function onInputBlur(ev) {
			if (ev.target.value.trim() === '') {
				classie.remove(ev.target.parentNode, 'input--filled');
			}
		}
	})();
</script>
<style>
	.el-form-item {
		margin-bottom: 0px;
	}

	.el-form-item__content {
		line-height: initial;
	}

	.custom-body {
	}

	.class-1 {
		color: #333333;
		background-image: url(${request.contextPath}/statics/images/login-bg.jpg);
		outline: none;
		outline-offset: -1px;
		background-size: cover;
		background-position: center;
		height: 100%;
		max-width: 100%;
		align-items: center;
		flex-direction: row;
		display: flex;
		justify-content: center;
		animation-duration: 1s;
		width: 100%;
		background-repeat: no-repeat;
	}

	.class-2 {
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 540px;
		max-width: 100%;
		background-color: rgba(255, 255, 255, 1);
		flex-direction: row;
		display: flex;
		animation-duration: 1s;
		border-radius: 12px;
		width: 1000px;
		background-repeat: no-repeat;
		overflow: hidden;
	}

	.class-3 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 100%;
		max-width: 100%;
		align-items: flex-start;
		flex-direction: row;
		display: flex;
		justify-content: flex-start;
		animation-duration: 1s;
		width: 460px;
		background-repeat: no-repeat;
	}

	.class-4 {
		height: 100%;
		animation-duration: 1s;
		width: 100%;
	}

	.class-5 {
		color: #333333;
		outline: none;
		padding-bottom: 20px;
		outline-offset: -1px;
		flex: 1;
		padding-top: 20px;
		height: 100%;
		max-width: 100%;
		align-items: center;
		flex-direction: column;
		display: flex;
		justify-content: flex-start;
		animation-duration: 1s;
		width: 200px;
		background-repeat: no-repeat;
	}

	.class-6 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		max-width: 100%;
		flex-direction: column;
		display: flex;
		animation-duration: 1s;
		width: 330px;
		background-repeat: no-repeat;
		margin-top: 20px;
	}

	.class-7 {
		color: #333333;
		word-wrap: break-word;
		display: inline-block;
		animation-duration: 1s;
		font-size: 36px;
		line-height: 1.4;
		margin-bottom: 20px;
	}

	.class-8 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 80px;
		max-width: 100%;
		flex-direction: column;
		display: flex;
		justify-content: flex-end;
		animation-duration: 1s;
		width: 100%;
		background-repeat: no-repeat;
	}

	.class-9 {
		color: #BBBBBB;
		word-wrap: break-word;
		display: inline-block;
		animation-duration: 1s;
		font-size: 12px;
		line-height: 1.4;
	}

	.class-10 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 40px;
		max-width: 100%;
		align-items: center;
		flex-direction: row;
		display: flex;
		animation-duration: 1s;
		width: 100%;
		background-repeat: no-repeat;
	}

	.class-11 {
		color: #333333;
		word-wrap: break-word;
		display: inline-block;
		animation-duration: 1s;
		font-size: 14px;
		line-height: 1.4;
	}

	.class-12 {
		margin-right: auto;
		animation-duration: 1s;
		background-color: #eee;
		border-radius: 1px;
		width: 100%;
		height: 1px;
		margin-left: auto;
	}

	.class-13 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 80px;
		max-width: 100%;
		flex-direction: column;
		display: flex;
		justify-content: flex-end;
		animation-duration: 1s;
		width: 100%;
		background-repeat: no-repeat;
	}

	.class-14 {
		color: #BBBBBB;
		word-wrap: break-word;
		padding-bottom: 10px;
		display: inline-block;
		animation-duration: 1s;
		font-size: 14px;
		line-height: 1.4;
	}

	.class-15 {
		margin-right: auto;
		animation-duration: 1s;
		background-color: #eee;
		border-radius: 1px;
		width: 100%;
		height: 1px;
		margin-left: auto;
	}

	.class-16 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 80px;
		max-width: 100%;
		align-items: flex-end;
		flex-direction: row;
		display: flex;
		justify-content: flex-start;
		animation-duration: 1s;
		background-repeat: no-repeat;
	}

	.class-17 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		flex: 1;
		height: 80px;
		max-width: 100%;
		flex-direction: column;
		display: flex;
		justify-content: flex-end;
		animation-duration: 1s;
		width: 200px;
		background-repeat: no-repeat;
	}

	.class-18 {
		color: #BBBBBB;
		word-wrap: break-word;
		display: inline-block;
		animation-duration: 1s;
		font-size: 14px;
		line-height: 1.4;
		margin-bottom: 10px;
	}

	.class-19 {
		margin-right: auto;
		animation-duration: 1s;
		background-color: #eee;
		border-radius: 1px;
		width: 100%;
		height: 1px;
		margin-left: auto;
	}

	.class-20 {
		cursor: pointer;
		color: #333333;
		margin-right: 10px;
		outline-offset: -1px;
		height: 40px;
		max-width: 100%;
		align-items: center;
		flex-direction: row;
		display: flex;
		justify-content: center;
		margin-left: 10px;
		animation-duration: 1s;
		width: 88px;
		background-repeat: no-repeat;
		margin-bottom: 0.85em;
	}

	.class-21 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		max-width: 100%;
		align-items: flex-end;
		flex-direction: column;
		display: flex;
		justify-content: flex-end;
		animation-duration: 1s;
		background-repeat: no-repeat;
		margin-bottom: 0.85em;
	}

	.class-22 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		max-width: 100%;
		flex-direction: column;
		display: flex;
		animation-duration: 1s;
		background-repeat: no-repeat;
	}

	.class-23 {
		color: #BBBBBB;
		word-wrap: break-word;
		display: inline-block;
		animation-duration: 1s;
		font-size: 12px;
		line-height: 1.4;
	}

	.class-24 {
		cursor: pointer;
		color: #0099FF;
		word-wrap: break-word;
		display: inline-block;
		animation-duration: 1s;
		font-size: 12px;
		line-height: 1.4;
	}

	.class-25 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		height: 40px;
		max-width: 100%;
		flex-direction: row;
		display: flex;
		animation-duration: 1s;
		width: 100px;
		background-repeat: no-repeat;
	}

	.class-26 {
		background-color: #0099ff;
	}

	.class-27 {
		color: #333333;
		outline: none;
		outline-offset: -1px;
		max-width: 100%;
		align-items: center;
		flex-direction: row;
		display: flex;
		animation-duration: 1s;
		width: 100px;
		background-repeat: no-repeat;
		margin-top: 20px;
	}

	.class-28 {
		color: #333333;
		outline: 1px dashed hsla(0, 0%, 66.7%, .7);
		outline-offset: -1px;
		height: 14px;
		max-width: 100%;
		flex-direction: row;
		display: flex;
		animation-duration: 1s;
		width: 14px;
		background-repeat: no-repeat;
	}

	.class-29 {
		color: #999999;
		word-wrap: break-word;
		display: inline-block;
		margin-left: 10px;
		animation-duration: 1s;
		font-size: 14px;
		line-height: 1.4;
	}

	@media (max-width: 768px) {
	}

	.input__label--hoshi::before {
		content: '';
		position: absolute;
		top: 1px;
		left: 0;
		width: 100%;
		height: calc(100% - 10px);
		border-bottom: 1px solid #B9C1CA;
	}
</style>
