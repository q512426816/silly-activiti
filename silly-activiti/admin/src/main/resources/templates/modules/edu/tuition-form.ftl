<el-dialog id="form" title="缴费信息" :visible.sync="dialogVisible" :width="dialogWidth" :fullscreen="fullscreen"
           :close-on-click-modal="false" v-cloak>
	<el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
		<el-row>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="人员信息" prop="personId">
					<xh-select v-model="form.personId" res="person"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="推广编号" prop="agentCode">
					<el-input v-model="form.agentCode"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入推荐人的推广编号">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="了解渠道" prop="channel">
					<xh-select v-model="form.channel" dict-type="EDU_CHANNEL"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="缴费金额" prop="payMoney">
					<el-input v-model="form.payMoney"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入缴费金额(元)">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="应缴金额" prop="needPayMoney">
					<el-input v-model="form.needPayMoney"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入应缴金额(元)">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="购买保险" prop="hasInsurance">
					<xh-select v-model="form.hasInsurance" dict-type="YES_NO"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="状态" prop="status">
					<xh-select v-model="form.status" dict-type="PAY_STATUS"></xh-select>
				</el-form-item>
			</el-col>
		</el-row>

		<el-row>
			<el-col :xs="24">
				<el-form-item label="备注" prop="remark">
					<el-input v-model="form.remark"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入备注">
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
				dialogWidth: '50%',
				fullscreen: false,
				dialogVisible: false,
				form: {},
				rules: {
					personId: [{
						"required": true,
						"message": "客户必须填写"
					}],
					channel: [{
						"required": true,
						"message": "缴费金额必须填写"
					}],
					payMoney: [{
						"required": true,
						"message": "缴费金额必须填写"
					}],
					needPayMoney: [{
						"required": true,
						"message": "应缴金额必须填写"
					}],
					hasInsurance: [{
						"required": true,
						"message": "是否购买保险必须填写"
					}],
					status: [{
						"required": true,
						"message": "状态必须填写"
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
		mounted() {
			var that = this;
			window.onresize = function () {
				that.setDialog()
			}
		},
		computed: {},
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
				this.$refs.form.validate(function (valid) {
					if (valid) {
						that.saveDisabled = true;
						var url = xh.base + "/edu/tuition/" + (!!that.form.id ? "update" : "save");
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
				xh.http.get(xh.base + "/edu/tuition/info/" + id).then(function (data) {
					if (data.code === 0) {
						that.form = data.entity;
					} else {
						that.$notify({
							title: '失败',
							message: data.msg,
							type: 'warning'
						});
					}
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
