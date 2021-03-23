<script type="text/x-template" id="my-select">
	<el-select v-if="!loading" style="width: 100%" v-bind="$props" @change="$emit('change',$event)" v-model="select"
	           filterable reserve-keyword :placeholder="placeholder" :loading="loading" clearable>
		<slot name="options"></slot>
		<el-option v-for='item in dataList' :key="item[prop.value]" :value="item[prop.value]"
		           :label="item[prop.label]">
		</el-option>
	</el-select>
	<div v-else>
		<i class="el-icon-loading"></i> 数据加载中
	</div>
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
			index: Number,
			dictType: String,
			url: String,
			res: String,
			// vtmk => variableTypeMap 的 KEY
			vtmk: String,
			actVar: String,
			nodeVar: String,
			masterVar: String,
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

		Vue.component('my-select', {
			template: '#my-select',
			props: props,
			data() {
				return {
					loading: false,
					dataList: [],
					select: []
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
					this.setSelect();
				},
				res: function (n, o) {
					this.load();
					this.$emit('init', n, this);
				}
			},
			methods: {
				init() {
					this.load();
					this.$emit('init', this.$parent.prop, this);
				},
				load() {
					if (this.dictType) {
						this.res = "dict";
						this.searchData.type = this.dictType;
					}
					this.setRes(this.res);
					this.list();
					this.setSelect();
				},
				setRes(res) {
					if (!res) {
						return;
					}
					this.url = this.url || my.base + '/common/select/' + res;
				},
				list(obj) {
					if (!obj) {
						obj = {};
					}
					var that = this;
					var url = that.url;
					if (!url) {
						return;
					}
					var searchData = obj.searchData || that.searchData;
					if (searchData && searchData.unQuery) {
						return;
					}
					if (that.loading) {
						return;
					}
					that.loading = true;
					my.http.post(url, JSON.stringify(searchData), {headers: {"Content-Type": "application/json"}})
						.then(function (res) {
							that.loading = false;
							that.dataList = res.list;
						}).catch(function (err) {
						that.loading = false;
						console.log(err);
					});
				},
				setSelect() {
					var that = this;
					//配合 multiple 使用
					if (that.multiple) {
						if (that.value instanceof Array) {
							that.select = that.value;
						} else if (that.value) {
							that.select = that.value.split(",")
						} else {
							that.select = []
						}
					} else {
						that.select = that.value
					}
				}
			},
			created() {
				this.init();
			}
		});
	})();
</script>