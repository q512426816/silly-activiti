<el-dialog id="form" title="人员信息" :visible.sync="dialogVisible" :width="dialogWidth" :fullscreen="fullscreen"
		   :close-on-click-modal="false" v-cloak>
	<el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
		<el-row>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="人员名称" prop="name">
					<el-input v-model="form.name"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入人员名称">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="人员编号" prop="code">
					<el-input v-model="form.code"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入人员编号">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="人员类型" prop="type">
					<xh-select v-model="form.type" dict-type="PERSON_TYPE"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="性别" prop="gender">
					<xh-select v-model="form.gender" dict-type="GENDER"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="身份证" prop="idCardNo">
					<el-input v-model="form.idCardNo"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入身份证">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="居住地" prop="residence">
					<el-input v-model="form.residence"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入居住地">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="生日" prop="birthday">
					<el-date-picker
							v-model="form.birthday"
							type="date"
							value-format="yyyy-MM-dd HH:mm:ss"
							:disabled="false"
							:style="{width:  '100%'}"
							placeholder="请选择生日">
					</el-date-picker>
				</el-form-item>
		</el-row>
		<el-row>
			<el-col :xs="24">
				<el-form-item label="备注" prop="remark">
					<el-input
							type="textarea"
							autosize
							placeholder="请输入备注"
							v-model="form.remark">
					</el-input>
				</el-form-item>
			</el-col>
		</el-row>
	</el-form>
	<div slot="footer">
		<el-button size="mini" @click="dialogVisible = false">取 消</el-button>
		<el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保存</el-button>
	</div>
</el-dialog>
<script>
	var formVue = new Vue({
		el: '#form',
		data: function () {
			return {
				saveDisabled: false,
				dialogVisible: false,
				dialogWidth: '50%',
				fullscreen: false,
				form: {
					name: '',
					code: ''
				},
				managerRoleidOptions: [],
				rules: {
					// 客户名
					name: [{
						"required": true,
						"message": "人员名必须填写"
					}, {
						min: 2,
						max: 8,
						message: '人员名长度为2-5个字符!',
						trigger: 'change'
					}],
					code: [{
						"required": true,
						"message": "人员编号必须填写"
					}, {
						min: 4,
						max: 12,
						message: '人员编号长度为4-12个字符!',
						trigger: 'change'
					}],
					gender: [{
						"required": true,
						"message": "性别必须选择"
					}],
					type: [{
						"required": true,
						"message": "人员类型必须选择"
					}]
				}
			};
		},
		watch: {
			dialogVisible: function (v) {
				if (!v) {
					this.$refs.form.resetFields();
					this.form.id = null;
				}
			}
		},
		computed: {},
		mounted() {
			var that = this;
			window.onresize = function () {
				that.setDialog()
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
			save: function () {
				var that = this;
				var url = baseURL + "/res/person/save";

				this.$refs.form.validate(function (valid) {
					if (valid) {
						that.saveDisabled = true;
						var data = JSON.parse(JSON.stringify(that.form));
						xh.http.post(url, data, {headers: {"Content-Type": "application/json"}}).then(function (data) {
							if (data.code === 0) {
								that.$notify({
									title: '成功',
									message: '保存成功',
									type: 'success'
								});
								that.saveDisabled = false;
								that.dialogVisible = false;
								that.form.id = null;
								indexVue.list();
							} else {
								that.$notify({
									title: '失败',
									message: data.msg,
									type: 'warning'
								});
								that.saveDisabled = false;
							}
						});
					} else {
						return false;
					}
				});
			},
			get: function (id) {
				var that = this;
				xh.http.get(baseURL + "/res/person/info/" + id).then(function (data) {
					that.form = data.entity;
				}).catch(function (err) {
					console.log(err);
				});
			}
		},
		created: function () {
			this.setDialog();
		}
	});
</script>
