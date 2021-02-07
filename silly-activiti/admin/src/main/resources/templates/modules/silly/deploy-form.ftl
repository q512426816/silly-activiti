<el-dialog id="form" title="流程文件部署" :visible.sync="dialogVisible" :width="dialogWidth" :fullscreen="fullscreen"
           :close-on-click-modal="false" v-cloak>
	<el-upload ref="upload"
	           :action="xh.base+'/silly/deploy'"
	           :on-preview="handlePreview"
	           :on-remove="handleRemove"
	           :file-list="fileList"
	           :auto-upload="false">
		<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
		<el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">执行部署</el-button>
		<div slot="tip" class="el-upload__tip">~</div>
	</el-upload>
</el-dialog>
<script>
	var formVue = new Vue({
		el: '#form',
		data: function () {
			return {
				dialogVisible: false,
				dialogWidth: '50%',
				fullscreen: false,
				fileList: []
			};
		},
		watch: {
			dialogVisible: function (v) {
				if (!v) {
					this.$refs.upload.resetFields();
				}
			}
		},
		methods: {
			open: function (id) {
				if (id) {
					this.get(id);
				}

				this.$nextTick(function () {
					this.dialogVisible = true;
				});
			},
			setDialog: function () {
				this.dialogWidth = getDiagWidth();
				this.fullscreen = isMobile();
			},
			submitUpload() {
				this.$refs.upload.submit();
			},
			handleRemove(file, fileList) {
				console.log(file, fileList);
			},
			handlePreview(file) {
				console.log(file);
			}
		},
		created: function () {
			this.setDialog();
		}
	});
</script>
