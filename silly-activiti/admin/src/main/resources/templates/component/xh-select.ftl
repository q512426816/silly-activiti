<script type="text/x-template" id="xh-select">
	<el-select style="width: 100%" v-bind="$props" @change="$emit('change',$event)" v-model="select"
	           filterable reserve-keyword :placeholder="placeholder">
		<el-option v-for='item in dataList' :key="item[prop.value]" :value="item[prop.value]"
		           :label="item[prop.label]">
		</el-option>
	</el-select>
</script>
<script>
	(function () {
		var props = Object.assign({
			placeholder: {
				type: String,
				default() {
					return "请选择";
				}
			},
			dictType: String,
			url: String,
			res: String,
			searchData: {
				type: Object,
				default() {
					return {};
				}
			},
			prop: {
				type: Object,
				default() {
					return {
						isDefault: true,
						value: 'id',
						label: 'name'
					};
				}
			}
		}, Vue.options.components.ElSelect.options.props);

		Vue.component('xh-select', {
			template: '#xh-select',
			props: props,
			data() {
				return {
					dataList: [],
					select: this.value
				}
			},
			watch: {
				select: function (n, o) {
					if (this.multiple) {
						this.$emit("input", this.select.join(','));
						return
					}
					this.$emit("input", n)
				},
				value: function (n, o) {
					this.init()
				}
			},
			methods: {
				init() {
					if (this.dictType) {//字典类型拉取字典数据
						this.url = this.url || xh.base + '/common/select/dict';
						this.searchData.type = this.dictType;
						this.prop = this.prop.isDefault ? {value: 'code', label: 'value'} : this.prop;
						this.list()
					} else if (this.res) {
						this.setRes(this.res);
						this.list()
					} else if (this.url) {//拉取列表数据
						this.list()
					}

					//配合 multiple 使用
					if (this.multiple) {
						if (this.value) {
							this.select = this.value.split(",")
						} else {
							this.select = []
						}
					} else {
						this.select = this.value
					}
				},
				setRes(res) {
					if (res) {
						switch (res) {
							case 'person':
								this.url = this.url = this.url || xh.base + '/common/select/person';
								break;
							default:
								console.warn("xh 下拉选：此资源没有定义指向~" + res);
						}
					}
				},
				list() {
					var that = this;
					that.loading = true;
					xh.http.get(that.url, that.searchData).then(function (res) {
						that.loading = false;
						that.dataList = res.page.list;
						that.page = res.page;
					}).catch(function (err) {
						console.log(err);
					});
				}
			},
			created() {
				this.init()
			}
		});

	})()
</script>



