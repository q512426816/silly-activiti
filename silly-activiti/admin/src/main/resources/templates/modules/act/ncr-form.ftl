<el-dialog id="form" title="招生员信息" :visible.sync="dialogVisible" :width="dialogWidth" :fullscreen="fullscreen"
           :close-on-click-modal="false" v-cloak>
	<el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
		<el-row>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="流程KEY" prop="processKey">
					<el-input v-model="form.processKey"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="流程KEY">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="流程版本" prop="processVersion">
					<el-input v-model="form.processVersion"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="流程版本">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="24" :lg="24">
				<el-form-item label="审批人ID" prop="approveUserId">
					<el-input v-model="form.approveUserId"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="审批人ID">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="招生人员" prop="createUserId">
					<xh-select v-model="form.createUserId" res="person"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="推广编号" prop="agentCode">
					<el-input v-model="form.agentCode"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入推广编号">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="加入时间" prop="joinDate">
					<el-date-picker v-model="form.joinDate"
					                type="date"
					                value-format="yyyy-MM-dd HH:mm:ss"
					                :disabled="false"
					                :style="{width:  '100%'}"
					                placeholder="请选择加入时间">
					</el-date-picker>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="推广等级" prop="grade">
					<xh-select v-model="form.grade" dict-type="GENERALIZE_GRADE"></xh-select>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="招收人数" prop="enrollment">
					<el-input v-model="form.enrollment"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入招收人数">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="分红" prop="dividend">
					<el-input v-model="form.dividend"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入分红">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="返点" prop="rebate">
					<el-input v-model="form.rebate"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入返点">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="年终福利" prop="yearEndBonus">
					<el-input v-model="form.yearEndBonus"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入年终福利">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="额外福利" prop="extraBonus">
					<el-input v-model="form.extraBonus"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入额外福利">
					</el-input>
				</el-form-item>
			</el-col>
			<el-col :xs="24" :sm="12" :lg="8">
				<el-form-item label="供应商优惠券" prop="coupon">
					<el-input v-model="form.coupon"
					          :disabled="false"
					          :style="{width:  '100%'}"
					          :clearable="true"
					          placeholder="请输入相关供应商的优惠券">
					</el-input>
				</el-form-item>
			</el-col>
		</el-row>
		<el-row>
			<el-col :xs="24">
				<el-form-item label="备注" prop="remark">
					<el-input v-model="form.remark"
					          type="textarea"
					          autosize
					          placeholder="请输入备注">
					</el-input>
				</el-form-item>
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
				masterMap: {id: "id", processKey: "processKey", processVersion: "processVersion"},
				nodeMap: {grade: "grade"},
				variableTypeMap: {createUserId: "list"},
				activitiHandlerMap: {createUserId: 'string', approveUserId: 'string'},
				form: {
					processKey: "PBTS_DELIVERY_VEHICLE_V1",
					processVersion: "V1",
					createUserId: "",
					agentCode: "",
					joinDate: "",
					grade: "",
					approveUserId: null
				},
				managerRoleidOptions: [],
				rules: {
					agentCode: [{
						"required": true,
						"message": "推广编号必须填写"
					}, {
						min: 3,
						max: 8,
						message: '推广编号长度为3-8个字符!',
						trigger: 'change'
					}],
					createUserId: [{
						"required": true,
						"message": "招生员必须填写"
					}],
					joinDate: [{
						"required": true,
						"message": "加入时间必须填写"
					}],
					grade: [{
						"required": true,
						"message": "推广等级必须填写"
					}]
				}
			};
		},
		computed: {
			saveData: function () {
				var data = this.form;
				var myData = {
					master: {
						processKey: data.processKey,
						processVersion: data.processVersion
					},
					node: {
						variableList: []
					}
				};
				var masterMap = this.masterMap;
				var nodeMap = this.nodeMap;
				var variableTypeMap = this.variableTypeMap;
				var activitiHandlerMap = this.activitiHandlerMap;
				for (var key in data) {
					if (key && data.hasOwnProperty(key)) {
						var value = data[key];
						if (masterMap[key]) {
							myData.master[masterMap[key]] = value;
						}
						if (nodeMap[key]) {
							myData.node[nodeMap[key]] = value;
						}
						myData.node.variableList.push({
							variableType: variableTypeMap[key] || 'string',
							variableName: key,
							variableText: value,
							activitiHandler: activitiHandlerMap[key]
						})
					}
				}

				return myData;
			}
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
						var url = xh.base + "/silly/ncr/write/save";
						var data = JSON.parse(JSON.stringify(that.saveData));
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
						}).catch(function (err) {
							that.$notify({
								title: '失败',
								message: err,
								type: 'warning'
							});
							that.saveDisabled = false;
						});
					} else {
						return false;
					}
				});
			},
			get: function (id) {
				var that = this;
				xh.http.get(xh.base + "/silly/ncr/read/info/" + id).then(function (data) {
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
