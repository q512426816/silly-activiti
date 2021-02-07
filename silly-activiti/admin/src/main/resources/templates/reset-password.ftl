<!-- 修改密码 -->
<div id="reset-password" class="reset-password">
	<el-dialog title="修改密码" :visible.sync="isShow" width="30%" :close-on-click-modal="false">
		<el-scrollbar class="ms-scrollbar" style="height: 100%;">
			<el-form :model="resetPasswordForm" ref="resetPasswordForm" :rules="resetPasswordFormRule"
			         label-width='100px' size="mini">
				<el-form-item label="旧密码" prop="password">
					<el-input v-model="resetPasswordForm.password" size="mini" autocomplete="off"
					          show-password></el-input>
				</el-form-item>
				<el-form-item label="新密码" prop="newPassword">
					<el-input v-model="resetPasswordForm.newPassword" size="mini" autocomplete="off"
					          show-password></el-input>
				</el-form-item>
				<el-form-item label="确认新密码" prop="newConfirmPassword">
					<el-input v-model="resetPasswordForm.newConfirmPassword" size="mini" autocomplete="off"
					          show-password></el-input>
				</el-form-item>

				<el-form-item label="账号">
					<el-input v-model="resetPasswordForm.username" size="mini" autocomplete="off" readonly
					          disabled></el-input>
				</el-form-item>
			</el-form>
		</el-scrollbar>
		<div slot="footer" class="dialog-footer">
			<el-button size="mini"
			           @click="isShow = false;resetPasswordForm.password = '';resetPasswordForm.newPassword = ''">取 消
			</el-button>
			<el-button type="primary" size="mini" @click="updatePassword">更新密码</el-button>
		</div>
	</el-dialog>
</div>
<script>
	var resetPasswordVue = new Vue({
		el: '#reset-password',
		data: {
			// 模态框的显示
			isShow: false,
			resetPasswordForm: {
				username: '',
				password: '',
				newPassword: '',
				newConfirmPassword: ''
			},
			resetPasswordFormRule: {
				password: [{
					required: true,
					message: '请输入旧密码',
					trigger: 'blur'
				}, {
					min: 5,
					max: 20,
					message: '长度在 5 到 20 个字符',
					trigger: 'blur'
				}],
				newPassword: [{
					required: true,
					message: '请输入新密码',
					trigger: 'blur'
				}, {
					min: 6,
					max: 20,
					message: '长度在 6 到 20 个字符',
					trigger: 'blur'
				}],
				newConfirmPassword: [{
					required: true,
					message: '请再次输入确认密码',
					trigger: 'blur'
				}, {
					min: 6,
					max: 20,
					message: '长度在 6 到 20 个字符',
					trigger: 'blur'
				}, {
					validator: function (rule, value, callback) {
						if (resetPasswordVue.resetPasswordForm.newPassword === value) {
							callback();
						} else {
							callback('新密码和确认新密码不一致');
						}
					}
				}]
			}
		},
		methods: {
			// 更新密码
			updatePassword: function () {
				var that = this;
				this.$refs['resetPasswordForm'].validate(function (valid) {
					if (valid) {
						xh.http.post(xh.base + "/sys/user/password", that.resetPasswordForm).then(function (data) {
							if (data.code === 0) {
								that.resetPasswordForm.password = '';
								that.resetPasswordForm.newPassword = '';
								that.isShow = false;
								that.$message.success("修改成功");
							} else {
								that.$message.error(data.msg);
							}
						}, function (err) {
							that.$message.error(err);
						});
					}
				});
			}
		}
	});
</script>
