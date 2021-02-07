<!-- 退出系统 -->
<div id="exit-system" class="exit-system">
    <el-dialog title="退出提示" :visible.sync="isShow" width="380px">
        <span>确认退出</span>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" @click="isShow = false">取 消</el-button>
            <el-button size="mini" type="danger" @click="loginOut">退出</el-button>
        </div>
    </el-dialog>
</div>
<script>
    var exitSystemVue = new Vue({
        el: '#exit-system',
        data: {
            isShow: false // 模态框的显示
        },
        methods: {
            loginOut: function () {
                var that = this;
                xh.http.get(xh.base + "/logout").then(function (data) {
                    isShow = false;
                    location.href = xh.base + "/login";
                }, function (err) {
                    that.$message.error(data.msg);
                });
            }
        }
    });
</script>
