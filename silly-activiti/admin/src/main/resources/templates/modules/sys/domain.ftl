<!DOCTYPE html>
<html>
<head>
	<title>数据字典表</title>
    <#include "../../header.ftl">
</head>
<body>
<div id="index" class="ms-index" style="overflow-y: auto;" v-cloak>
	<el-row :gutter="12">
		<el-col :xs="24" :sm="12" :lg="8">
			<el-card shadow="hover" style="height:470px">
				<div slot="header" class="clearfix">
					<span>服务器信息</span>
				</div>
				<el-timeline>
					<el-timeline-item>
						<span>服务器名称：</span>
						<el-tag>{{form.sys.computerName}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>操作系统：</span>
						<el-tag>{{form.sys.osName}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>服务器IP：</span>
						<el-tag>{{form.sys.computerIp}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>系统架构：</span>
						<el-tag>{{form.sys.osArch}}</el-tag>
					</el-timeline-item>
				</el-timeline>
			</el-card>
		</el-col>

		<el-col :xs="24" :sm="12" :lg="8">
			<el-card shadow="hover" style="height:470px">
				<div slot="header" class="clearfix">
					<span>CPU</span>
				</div>
				<el-timeline>
					<el-timeline-item :color="getColor(form.cpu.cpuNum,4,2,1)">
						<span>核心数：</span>
						<el-tag>{{form.cpu.cpuNum + '核'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.cpu.used,20,60,80)">
						<span>用户使用率：</span>
						<el-tag>{{form.cpu.used + '%'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.cpu.sys,20,60,80)">
						<span>系统使用率：</span>
						<el-tag>{{form.cpu.sys + '%'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.cpu.free,80,50,20)">
						<span>当前空闲率：</span>
						<el-tag>{{form.cpu.free + '%'}}</el-tag>
					</el-timeline-item>
				</el-timeline>
			</el-card>
		</el-col>

		<el-col :xs="24" :sm="12" :lg="8">
			<el-card shadow="hover" style="height:470px">
				<div slot="header" class="clearfix">
					<span>磁盘状态</span>
				</div>
				<template>
					<el-table
							:data="form.sysFiles" stripe
							style="width: 100%">
						<el-table-column
								prop="dirName"
								label="盘符路径"
								width="80">
						</el-table-column>
						<el-table-column
								prop="sysTypeName"
								label="文件系统"
								width="80">
						</el-table-column>
						<el-table-column
								prop="typeName"
								label="盘符类型">
						</el-table-column>
						<el-table-column
								prop="total"
								label="总大小">
						</el-table-column>
						<el-table-column
								prop="free"
								label="已用大小">
						</el-table-column>
						<el-table-column
								prop="used"
								label="已用大小">
						</el-table-column>
						<el-table-column
								prop="usage"
								label="使用率">
						</el-table-column>
					</el-table>
				</template>
			</el-card>
		</el-col>

		<el-col :xs="24" :sm="12" :lg="8">
			<el-card shadow="hover" style="height:470px">
				<div slot="header" class="clearfix">
					<span>内存</span>
				</div>
				<el-timeline>
					<el-timeline-item :color="getColor(form.mem.total,8,4,2)">
						<span>总内存：</span>
						<el-tag>{{form.mem.total + 'GB'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.mem.used,2,4,8)">
						<span>已用内存：</span>
						<el-tag>{{form.mem.used + 'GB'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.mem.free,6,3,1)">
						<span>剩余内存：</span>
						<el-tag>{{form.mem.free + 'GB'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.mem.usage,20,60,80)">
						<span>使用率：</span>
						<el-tag>{{form.mem.usage + '%'}}</el-tag>
					</el-timeline-item>
				</el-timeline>
			</el-card>
		</el-col>

		<el-col :xs="24" :sm="12" :lg="8">
			<el-card shadow="hover" style="height:470px">
				<div slot="header" class="clearfix">
					<span>Java虚拟机信息</span>
				</div>
				<el-timeline>
					<el-timeline-item>
						<span>Java名称：</span>
						<el-tag>{{form.jvm.name}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>Java版本：</span>
						<el-tag>{{form.jvm.version}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>启动时间：</span>
						<el-tag>{{form.jvm.startTime}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>运行时长：</span>
						<el-tag>{{form.jvm.runTime}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>安装路径：</span>
						<el-tag>{{form.jvm.home}}</el-tag>
					</el-timeline-item>
					<el-timeline-item>
						<span>项目路径：</span>
						<el-tag>{{form.sys.userDir}}</el-tag>
					</el-timeline-item>
				</el-timeline>
			</el-card>
		</el-col>

		<el-col :xs="24" :sm="12" :lg="8">
			<el-card shadow="hover" style="height:470px">
				<div slot="header" class="clearfix">
					<span>JVM</span>
				</div>
				<el-timeline>
					<el-timeline-item :color="getColor(form.jvm.total,1000,500,300)">
						<span>总内存：</span>
						<el-tag>{{form.jvm.total + 'MB'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.jvm.used,200,500,1000)">
						<span>已用内存：</span>
						<el-tag>{{form.jvm.used + 'MB'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.jvm.free,1000,400,100)">
						<span>剩余内存：</span>
						<el-tag>{{form.jvm.free + 'MB'}}</el-tag>
					</el-timeline-item>
					<el-timeline-item :color="getColor(form.jvm.usage,20,60,80)">
						<span>使用率：</span>
						<el-tag>{{form.jvm.usage + '%'}}</el-tag>
					</el-timeline-item>
				</el-timeline>
			</el-card>
		</el-col>
	</el-row>
</div>
<script>
	var indexVue = new Vue({
		el: '#index',
		data: {
			loading: true,
			form: {
				sys: {},
				cpu: {},
				sysFiles: [],
				mem: {},
				jvm: {}
			}
		},
		methods: {
			getService: function () {
				var that = this;
				xh.http.get(xh.base + "/domain/service/info").then(function (data) {
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
			},
			getColor: function (num, successNum, warningNum, dangerNum) {
				if (!num || !successNum || !warningNum || !dangerNum) {
					return "#409EFF";
				}
				if (successNum > dangerNum) {
					if (num >= successNum) {
						return "#67C23A";
					} else if (num >= warningNum) {
						return "#E6A23C";
					} else {
						return "#F56C6C";
					}
				} else {
					if (num <= successNum) {
						return "#67C23A";
					} else if (num <= warningNum) {
						return "#E6A23C";
					} else {
						return "#F56C6C";
					}
				}

			}
		},
		mounted() {
			var that = this;
			var timer = setInterval(function () {
				that.getService();
			}, 20000);

			this.$once('hook:beforeDestroy', function () {
				clearInterval(timer)
			})
		},
		created: function () {
			this.getService();
		}
	});
</script>
</body>
</html>