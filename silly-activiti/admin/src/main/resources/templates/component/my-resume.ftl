<script type="text/x-template" id="my-upload">
	<el-upload
			:action="saveUrl"
			:on-preview="handlePreview"
			:on-remove="handleRemove"
			:on-success="handleSuccess"
			:before-upload="beforeUpload"
			:before-remove="beforeRemove"
			:file-list="fileList"
			:data="form">
		<div slot="trigger">
			<slot name="trigger"></slot>
		</div>
		<div v-show="loading"><i class="el-icon-loading"></i>数据加载中</div>
	</el-upload>
</script>
<script>
	(function () {
		var props = Object.assign({
			value: String,
		}, Vue.options.components.ElUpload.props);

		Vue.component('my-upload', {
			template: '#my-upload',
			props: props,
			data() {
				return {
					loading: false,
					saveUrl: my.base + '/common/silly/saveFile',
					deleteUrl: my.base + '/common/silly/deleteFile/',
					downloadUrl: my.base + '/common/silly/downloadFile/',
					getListUrl: my.base + '/common/silly/fileList/',
					form: {},
					fileList: [],
					defaultUuid: myUtil.getUuid()
				}
			},
			watch: {
				value: function (n, o) {
					this.$emit('input', n);
					this.init();
				}
			},
			methods: {
				init() {
					if (this.value && this.value !== this.defaultUuid) {
						this.getFileList(this.value);
					}
				},
				getFileList(groupId) {
					var that = this;
					if (!groupId) {
						return;
					}

					that.loading = true;
					setTimeout(function () {
						my.http.get(that.getListUrl + groupId).then(function (data) {
							if (data.code === 0 && data.data && data.data.rows) {
								that.fileList = data.data.rows.map(x => {
									x.name = x.attachName;
									return x;
								});
							}
							that.loading = false;
						}).catch(function (err) {
							that.$notify({
								title: '失败',
								message: err,
								type: 'warning'
							});
							that.loading = false;
						});
					}, 50)
				},
				setSaveForm() {
					this.form = Object.assign(this.form, {groupId: this.value})
				},
				handleRemove(file, fileList) {

				},
				handlePreview(file) {
					var that = this;
					window.open(that.downloadUrl + file.id);
				},
				handleSuccess(res, file, fileList) {
					file.id = res.data.id;
				},
				beforeUpload(file) {
					if (!this.value) {
						this.value = this.defaultUuid;
					}
					this.setSaveForm();
				},
				beforeRemove(file, fileList) {
					var that = this;
					return this.$confirm('确定移除文件 【' + file.name + "】 ？").then(() => {
						my.http.post(that.deleteUrl + file.id).then(function (data) {
							that.loading = false;
						}).catch(function (err) {
							that.$notify({
								title: '失败',
								message: err,
								type: 'warning'
							});
							that.loading = false;
						});
					});
				}
			},
			created() {
				this.init()
			}
		});

	})()
</script>