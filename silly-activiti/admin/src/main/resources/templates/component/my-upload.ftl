<script type="text/x-template" id="my-resume">
	<el-card class="box-card">
		<div slot="header" class="clearfix">
			<span>流程履历</span>
		</div>
		<el-steps v-if="!loading" direction="vertical" v-bind="$props" :active="resumeList.length"
		          :align-center="true">
			<el-step v-for="(item, index) in resumeList" :key="index" :status="getStatus(item.processType)">
				<div slot="title">{{item.processNodeName}}</div>
				<div slot="description" :style="item.processType==='next'?{color: 'black'}:{}">
					<div>
						完成时间：{{item.handleDate}}
						<el-divider direction="vertical"></el-divider>
						<span v-if="!!item.timeShow">
						处置耗时：【{{item.timeShow}}】
						</span>
					</div>
					<div>{{item.handleInfo}}</div>
				</div>
			</el-step>
		</el-steps>
		<div v-else>
			<i class="el-icon-loading"></i> 履历列表加载中
		</div>
	</el-card>
</script>
<script>
	(function () {
		var props = Object.assign({
			masterId: String
		}, Vue.options.components.ElSteps.props);

		Vue.component('my-resume', {
			template: '#my-resume',
			props: props,
			data() {
				return {
					loading: false,
					getResumeListUrl: my.base + '/common/silly/resumeList/',
					resumeList: []
				}
			},
			methods: {
				init() {
					this.getResumeList(this.masterId);
				},
				getStatus(type) {
					switch (type) {
						case 'next':
							return 'finish';
						case 'back':
							return 'error';
						case 'flow':
							return 'wait';
						case 'start':
							return 'process';
						case 'close':
							return 'success';
						default:
							return null;
					}
				},
				getResumeList(masterId) {
					var that = this;
					if (!masterId) {
						return;
					}
					that.loading = true;
					setTimeout(function () {
						my.http.get(that.getResumeListUrl + masterId).then(function (data) {
							if (data.code === 0 && data.data) {
								that.resumeList = data.data.map(x => {
									x.timeShow = dateUtil.timeShow(x.consumeTime);
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

				}
			},
			created() {
				this.init()
			}
		});
	})();
</script>