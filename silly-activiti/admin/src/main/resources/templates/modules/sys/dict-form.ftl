<el-dialog id="form" title="数据字典表" :visible.sync="dialogVisible" :width="dialogWidth" :fullscreen="fullscreen"
           :close-on-click-modal="false" v-cloak>
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
        <el-col :xs="24" :sm="12" :lg="8">
            <el-form-item label="字典名称" prop="name">
                <el-input v-model="form.name"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入字典名称">
                </el-input>
            </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="8">
            <el-form-item label="字典类型" prop="type">
                <el-input v-model="form.type"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入字典类型">
                </el-input>
            </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="8">
            <el-form-item label="字典码" prop="code">
                <el-input v-model="form.code"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入字典码">
                </el-input>
            </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="8">
            <el-form-item label="字典值" prop="value">
                <el-input v-model="form.value"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入字典值">
                </el-input>
            </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="8">
            <el-form-item label="排序" prop="orderNum">
                <el-input v-model="form.orderNum"
                          type="number"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入排序">
                </el-input>
            </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="8">
            <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入备注">
                </el-input>
            </el-form-item>
        </el-col>
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
                dialogWidth: 0,
                fullscreen: false,
                dialogVisible: false,
                form: {},
                rules: {
                    name: [{
                        "required": true,
                        "message": "字典名称必须填写"
                    }],
                    type: [{
                        "required": true,
                        "message": "字典类型必须填写"
                    }],
                    code: [{
                        "required": true,
                        "message": "字典码必须填写"
                    }],
                    value: [{
                        "required": true,
                        "message": "字典值必须填写"
                    }],
                    orderNum: [{
                        "required": true,
                        "message": "排序必须填写"
                    }],
                    remark: [{
                        "required": true,
                        "message": "备注必须填写"
                    }],
                    delFlag: [{
                        "required": true,
                        "message": "删除标记  -1：已删除  0：正常必须填写"
                    }],
                    createDate: [{
                        "required": true,
                        "message": "创建时间必须填写"
                    }],
                    createUserId: [{
                        "required": true,
                        "message": "创建人必须填写"
                    }],
                    updateDate: [{
                        "required": true,
                        "message": "更新时间必须填写"
                    }],
                    updateUserId: [{
                        "required": true,
                        "message": "更新人必须填写"
                    }],
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
                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var url = xh.base + "/sys/dict/" + (!!that.form.id ? "update" : "save");
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
                xh.http.get(xh.base + "/sys/dict/info/" + id).then(function (data) {
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
