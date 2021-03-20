(function () {
	/**
	 * 基础弹出层 vue
	 */
	var DialogVue = Vue.extend({
		el: '#form',
		data: function () {
			return {
				disabled: false,
				// 保存按钮加载中 标识
				saveDisabled: false,
				// 弹出框显示标识
				dialogVisible: false,
				// 弹出框宽度
				dialogWidth: '70%',
				// 是否全屏展示
				fullscreen: true,
				form: {}
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
		methods: {
			open: function (obj) {
				this.init(obj);
				this.$nextTick(function () {
					this.dialogVisible = true;
				});
			},
			init: function (obj) {

			},
			setDialog: function () {
				this.dialogWidth = getDiagWidth();
				this.fullscreen = isMobile();
			}
		},
		created: function () {
			this.setDialog();
		}
	});

	/**
	 * 定义专属 vue
	 */
	var SillyVue = DialogVue.extend({
		el: '#form',
		data: function () {
			return {
				id: null,
				taskId: null,
				saveUrl: '',
				getUrl: '',
				// 主表单对应属性及页面属性 {页面属性名：表单属性名}
				masterMap: {id: 'id'},
				// 节点表单对应属性及页面属性 {页面属性名：表单属性名}
				nodeMap: {taskId: 'taskId', nodeKey: 'nodeKey', id: 'masterId'},
				// 变量数据类型，目前支持【变量类型：string(默认)，list，list_list】 {页面属性名：变量类型}
				variableTypeMap: {},
				variableIgnoreArr: ["taskId"],
				// 工作流处置的变量类型，目前支持【变量类型：string(默认)，list，list_list】 {页面属性名：变量类型}
				activitiHandlerMap: {},
				// 表单数据
				form: {
					nodeKey: null
				},
				// 保存成功后执行的方法
				successFunc: function () {

				}
			};
		},
		methods: {
			init(obj) {
				if (obj.taskId) {
					this.taskId = obj.taskId;
				}
				if (obj.id) {
					this.id = obj.id;
					this.get(obj.id, this.form.nodeKey);
				}
			},
			setTypeMap(key, obj) {
				var value = obj.res;
				if (obj.multiple) {
					value += "s";
				}
				this.variableTypeMap[key] = value;
				var ignoreName = this.getMyIgnoreByKey(key, obj.multiple);
				if (ignoreName) {
					this.variableIgnoreArr.push(ignoreName);
				}

			},
			getMyIgnoreByKey(key, multiple) {
				if (key.toString().endsWith("Id")) {
					return key.substring(0, key.length - 2) + "Name";
				} else if (multiple && key.toString().endsWith("Ids")) {
					return key.substring(0, key.length - 3) + "Name";
				}
				return key + "Name";
			},
			checkField() {
				var that = this;
				var passFlag = true;
				if (that.$refs.list) {
					var length = that.$refs.list.length;
					for (var i = 0; i < length; i++) {
						that.$refs.list[i].validate(function (valid) {
							if (!valid) {
								passFlag = false;
							}
						});
					}
				}

				that.$refs.form.validate(function (valid) {
					if (!valid) {
						passFlag = false;
					}
				});

				if (!passFlag) {
					that.$notify({
						title: '表单未完成',
						message: '还有必填项未完成填写',
						type: 'warning'
					});
				}
				return passFlag;
			},
			// 保存的数据类型
			saveData: function (saveHandlerObj) {
				var that = this;
				if (!saveHandlerObj) {
					saveHandlerObj = {};
				}
				var data = saveHandlerObj.form;
				if (!data) {
					data = that.form;
					data.id = that.id;
					data.taskId = that.taskId;
				}

				var masterMap = saveHandlerObj.masterMap || that.masterMap;
				var nodeMap = saveHandlerObj.nodeMap || that.nodeMap;
				var variableTypeMap = saveHandlerObj.variableTypeMap || that.variableTypeMap;
				var activitiHandlerMap = saveHandlerObj.activitiHandlerMap || that.activitiHandlerMap;
				var variableIgnoreArr = saveHandlerObj.variableIgnoreArr || that.variableIgnoreArr;

				var myData = {
					master: {},
					node: {
						variableList: []
					}
				};
				for (var key in data) {
					if (key && data.hasOwnProperty(key)) {
						var value = data[key];
						var belong = 'variable';
						if (masterMap[key]) {
							myData.master[masterMap[key]] = value;
							belong = 'master';
						}
						if (nodeMap[key]) {
							myData.node[nodeMap[key]] = value;
							belong = 'node';
						}

						if (variableIgnoreArr.indexOf(key) >= 0) {
							// 不保存这个逼到变量表中
							continue;
						}

						var type = variableTypeMap[key] || 'string';
						var variableType = '';
						if (typeof(type) === 'string') {
							variableType = type;
						} else {
							variableType = type.res
						}

						myData.node.variableList.push({
							belong: belong,
							variableType: variableType,
							variableName: key,
							variableText: value ? value.toString() : null,
							activitiHandler: activitiHandlerMap[key]
						})
					}
				}

				return myData;
			},
			save: function (submit) {
				var that = this;
				var passFlag = true;
				if (submit) {
					passFlag = that.checkField();
				}
				if (passFlag) {
					var saveData = that.saveData();
					saveData.submit = submit;
					that.doSave(saveData)
				}
			},
			preSave(saveData) {},
			doSave: function (saveData) {
				var that = this;
				that.saveDisabled = true;
				that.preSave(saveData);
				my.http.post(that.saveUrl, saveData).then(function (data) {
					if (data.code === 0) {
						that.$notify({
							title: '成功',
							message: '保存成功',
							type: 'success'
						});
						that.dialogVisible = false;
						that.successFunc(data);
					} else {
						that.$notify({
							title: '失败',
							message: data.msg,
							type: 'warning'
						});
					}
					that.saveDisabled = false;
				}).catch(function (err) {
					that.$notify({
						title: '失败',
						message: err,
						type: 'warning'
					});
					that.saveDisabled = false;
				});
			},
			afterGet() {

			},
			get: function (id, nodeKey) {
				var that = this;
				return my.http.get(that.getUrl, {masterId: id, nodeKey: nodeKey}).then(function (data) {
					if (data.code === 0 && data.data) {
						that.form = data.data;
						that.form.id = id;
					}
				}).then(() => {
					that.afterGet();
				});
			}
		}
	});

	/**
	 * 定义专属 indexVue
	 */
	var SillyIndexVue = Vue.extend({
		el: '#index',
		data: function () {
			return {
				showSearch: false,
				loading: false,
				grid_selector: '#grid-table',
				pager_selector: "#grid-pager",
				pageUrl: '',
				delUrl: '',
				sizeHeight: 310,
				form: {},
				colModel: [],
				loadGrid: {}
			}
		},
		methods: {
			init() {
				var that = this;
				setTimeout(function () {
					that.reSizeHeight();
					that.loadGridTable();
				}, 50);
			},
			query: function () {
				this.reloadGridTable(this.form);
			},
			reset: function () {
				this.$refs.searchForm.resetFields();
				this.form = {};
				this.query();
			},
			reSizeHeight: function () {
				var strs = $.getWindowSize().toString().split(",");
				var jqgrid_height = strs[0] - this.sizeHeight;
				$(this.grid_selector).jqGrid('setGridHeight', jqgrid_height);
			},
			loadGridTable: function (form) {
				var that = this;
				var grid_selector = that.grid_selector;
				var pager_selector = that.pager_selector;

				if (that.loadGrid[grid_selector]) {
					that.reloadGridTable();
					return;
				}
				that.loadGrid[grid_selector] = true;
				that.loading = true;
				var $gridTable = jQuery(grid_selector);
				$gridTable.jqGrid({
					datatype: 'json',
					url: that.pageUrl,
					postData: form || that.form,
					colModel: that.colModel,
					viewrecords: true,
					rowNum: 20,
					rowList: [10, 20, 30, 50, 100, 500],
					pager: pager_selector,
					altRows: true,
					toppager: false,
					multiselect: false,
					rownumbers: true,
					loadComplete: function () {
						that.loading = false;
						$.changeGridTable.changeStyle(this);
					}
				});
				$.changeGridTable.changeSize([grid_selector, grid_selector + " ~ .widget-box"], that.reSizeHeight);
			},
			reloadGridTable: function (form) {
				var that = this;
				that.loading = true;
				var grid_selector = that.grid_selector;
				$(grid_selector).jqGrid('setGridParam', {
					postData: form || that.form
				}, true).trigger("reloadGrid");
			},
			del: function (rowId) {
				var that = this;
				var rowData = this.getRowData(rowId);
				that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					my.http.post(that.delUrl + rowData.id).then(function (data) {
						if (data.code === 0) {
							that.$notify({
								type: 'success',
								message: '删除成功!'
							});
							that.reloadGridTable();
						} else {
							that.$notify({
								title: '失败',
								message: data.msg,
								type: 'warning'
							});
						}
					});
				}).catch(function () {
					that.$notify({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			getSelectedIds: function () {
				return $(this.grid_selector).jqGrid("getGridParam", "selarrrow");
			},
			getRowData: function (rowId) {
				return $(this.grid_selector).jqGrid('getRowData', rowId);
			}
		},
		created: function () {
			var scripts = [null, null];
			$('.page-content-area').ace_ajax('loadScripts', scripts, function () {});

			this.init();

		}
	});

	// v-dialog-drag: 弹窗拖拽
	DialogVue.directive('dialogDrag', {
		bind(el, binding, vnode, oldVnode) {
			const dialogHeaderEl = el.querySelector('.el-dialog__header');
			const dragDom = el.querySelector('.el-dialog');
			dialogHeaderEl.style.cursor = 'move';

			// 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null);
			const sty = dragDom.currentStyle || window.getComputedStyle(dragDom, null);

			dialogHeaderEl.onmousedown = (e) => {
				// 鼠标按下，计算当前元素距离可视区的距离
				const disX = e.clientX - dialogHeaderEl.offsetLeft;
				const disY = e.clientY - dialogHeaderEl.offsetTop;

				// 获取到的值带px 正则匹配替换
				let styL, styT;

				// 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
				if (sty.left.includes('%')) {
					styL = +document.body.clientWidth * (+sty.left.replace(/\%/g, '') / 100);
					styT = +document.body.clientHeight * (+sty.top.replace(/\%/g, '') / 100);
				} else {
					styL = +sty.left.replace(/\px/g, '');
					styT = +sty.top.replace(/\px/g, '');
				}

				document.onmousemove = function (e) {
					// 通过事件委托，计算移动的距离
					const l = e.clientX - disX;
					const t = e.clientY - disY;

					// 移动当前元素
					dragDom.style.left = `${l + styL}px`;
					dragDom.style.top = `${t + styT}px`;

					//将此时的位置传出去
					//binding.value({x:e.pageX,y:e.pageY})
				};

				document.onmouseup = function (e) {
					document.onmousemove = null;
					document.onmouseup = null;
				};
			}
		}
	});


	window.DialogVue = DialogVue;
	window.SillyVue = SillyVue;
	window.SillyIndexVue = SillyIndexVue;
}());


