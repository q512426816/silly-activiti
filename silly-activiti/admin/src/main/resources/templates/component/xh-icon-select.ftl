<!--图标组件-->
<template id="xh-icon-select">
	<div v-cloak>
		<el-input v-model="icon">
			<template slot="prepend"><i style="line-height: 1;" class="iconfont" :class="icon"></i></template>
			<el-button slot="append" icon="el-icon-search" @click="openIconList"></el-button>
		</el-input>
		<el-dialog title="选择图标" :visible.sync="dialogVisible" width="80%" custom-class="ms-Icons" append-to-body>
			<el-main>
				<el-scrollbar style="height: 100%;">
					<div class="list">
						<div v-for="(item,index) in icons" :class="mark==index ? 'select' : ''"
						     @click="selected(item.font_class,index)">
							<i class="iconfont" :class="'icon-'+item.font_class"></i>icon-{{ item.font_class }}
						</div>
					</div>
				</el-scrollbar>
			</el-main>
			<div slot="footer">
				<el-button size="mini" @click="dialogVisible = false">取 消</el-button>
				<el-button size="mini" type="primary" @click="select">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>
<script type="text/javascript">
	Vue.component('xh-icon-select', {
		template: '#xh-icon-select',
		props: ["value"],
		data: function () {
			return {
				dialogVisible: false,
				icons: [],
				icon: '',
				mark: -1
			}
		},
		watch: {
			'dialogVisible': function (n, o) {
				if (n) {
					this.iconList();
				}
			},
			icon: function (n) {
				this.$emit("input", n);
			},
			value: function (n) {
				this.icon = n;
			}
		},
		methods: {
			//打开图表列表
			openIconList: function () {
				this.dialogVisible = true;
			},
			selected: function (value, index) {
				this.icon = 'icon-' + value;
				this.mark = index;
			},
			select: function () {
				this.dialogVisible = false;
			},
			iconList: function () {
				var that = this;
				xh.http.get(xh.base + "/statics/plugins/iconfont/1.0.0/iconfont.json", {}).then(
						function (data) {
							that.icons = data.glyphs;
						}).catch(function (err) {
					console.log(err);
				});
			}
		}
	})
</script>
<style>
	[v-cloak] {
		display: none;
	}

	.ms-Icons .list {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		line-height: 30px;
	}

	.ms-Icons .list div {
		width: 25%;
		cursor: pointer;
	}

	.ms-Icons .list .select {
		background-color: #eee;
	}

	.ms-Icons .list div i {
		padding-right: 5px;
	}

	.ms-Icons .list div:hover {
		background-color: #eee;
	}

	.ms-Icons .el-main {
		height: calc(100vh - 45vh);
		padding: 0 20px;
	}

	.ms-Icons .el-scrollbar__wrap {
		overflow-x: hidden;
	}
</style>