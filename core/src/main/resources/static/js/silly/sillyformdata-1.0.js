//      傻瓜前端传递参数 数据结构生成工具
//
//      objArr 根据后台接口参数设计
//		var objArr = [{
//			attr: "ma-var",
//			myEntity: "master"
//		}, {
//			attr: "re-var",
//			myEntity: "node"
//		}, {
//			attr: "bd-var",
//			myEntity: "base64Data",
//			myEntityKey: ".dataName",
//			myEntityValue: ".dataClob",
//			valueFun: function (value) {
//				return $.base64.btoa(value, true);
//			},
//			myEntityLabel: ".dataType"
//		}, {
//			attr: "my-var",
//			myEntity: "taskData.variableList",
//			myEntityIsList: true,
//			myEntityKey: ".variableName",
//			myEntityValue: ".variableText",
//			myEntityLabel: ".variableLabel"
//		}];
//      // 工作流数据存储对象
//		var actObj = {
//			attr: "act-var",
//			myEntity: "taskData.variableList",
//			myEntityIsList: true,
//			myEntityKey: ".variableName",
//			myEntityValue: ".variableText",
//			myEntityLabel: ".variableLabel"
//		};
/**
 *  @author QINY
 *  @since 1.0
 */
if (typeof jQuery === 'undefined') {
	throw new Error('需要引入jQuery!')
}

var SillyFormData = function () {};
SillyFormData.constructor = new SillyFormData();
SillyFormData.prototype.constructor = SillyFormData;

SillyFormData.prototype.checkLink = [function ($elem, showElem, showClass, emptyClass) {
	$elem.find("[my-check='need']").each(function () {
		if (!this.value) {
			var name = $(this).attr("_name");
			var value = null;
			if (name) {
				value = $elem.find("[name=" + name + "]").val();
			}
			if (!value) {
				$(this).addClass(emptyClass);
				$(this).parent().parent().addClass(showClass);
			}
		}
	});
}, function ($elem, showElem, showClass, emptyClass) {
	$elem.find("[my-check='checked']").each(function () {
		if (!this.checked) {
			$(this).addClass(emptyClass);
			$(this).parent().addClass(showClass);
		}
	});
}, function ($elem, showElem, showClass, emptyClass) {
	var radioObj = {};
	$elem.find("[my-check='radio']").each(function () {
		if (!radioObj[this.name] && this.checked) {
			radioObj[this.name] = this.checked;
			$(this).removeClass(emptyClass);
			$(this).parent().parent().removeClass(showClass);
		} else if (!radioObj[this.name]) {
			$(this).addClass(emptyClass);
			$(this).parent().parent().addClass(showClass);
		}
	});
}];

SillyFormData.prototype.checkData = function ($elem, showElem, showClass, emptyClass) {
	emptyClass = emptyClass || "need-empty";
	if (showElem) {
		showClass = showClass || 'needFillIn';
		$elem.find("." + showClass).removeClass(showClass);
		$elem.find("." + emptyClass).removeClass(emptyClass);
	} else {
		showClass = "";
		return false;
	}

	$.each(this.checkLink, function () {
		this($elem, showElem, showClass, emptyClass);
	});

	var passFlag = $elem.find("[act-var='nextUserId']").length === 0;
	$elem.find("[act-var='nextUserId']").each(function () {
		if (this.value) {
			passFlag = true;
		}
	});
	if (!passFlag) {
		return "下一步人获取失败！请确认信息是否填写完成！";
	}

	var returnInfo = false;

	var fillCount = $elem.find("." + showClass).length;
	if (fillCount > 0) {
		var errorIndex = 1;
		returnInfo = "还有【 " + fillCount + " 】项必填数据未填写！";
		$elem.find("." + showClass).each(function () {
			$(this).find("." + emptyClass).each(function () {
				var needInfo = $(this).attr("need-info");
				if (needInfo) {
					returnInfo = returnInfo + "<br/>" + (errorIndex++) + "、" + needInfo;
				}
			});
		});
	}
	return returnInfo;
};

SillyFormData.prototype.setMyData = function ($elem, objArr, actOption, formData) {
	var t = this;
	formData = formData || new FormData();
	var defOption = {
		count: 0,
		attr: "-",
		containNotAttr: false,
		myEntity: "",
		myEntityIsList: false,
		myEntityKey: "",
		myEntityValue: "",
		valueFun: function (data) {
			return data;
		},
		myEntityLabel: ""
	};
	var actObj = $.extend({}, defOption, actOption);
	var myObjArr = [];
	var hasSameAct = false;
	for (var i = 0; i < objArr.length; i++) {
		var myObj = $.extend({}, defOption, objArr[i]);
		myObjArr.push(myObj);
		if (myObj.myEntity === actObj.myEntity) {
			hasSameAct = true;
		}
	}
	$.each($elem.serializeArray(), function () {
		var tv = this.value;
		var tn = this.name;
		var aPropFlag = true;
		for (var k = 0; k < myObjArr.length; k++) {
			var $tElem = $elem.find("[name='" + tn + "']");
			var obj = myObjArr[k];
			var tAttrValue = $tElem.attr(obj.attr);
			if (!tAttrValue) {
				// 下拉多选值
				$tElem = $elem.find("[_name='" + tn + "']");
				if ($tElem.length > 0) {
					tAttrValue = $tElem.attr(obj.attr);
				}
			}
			var aProp = false;
			if (hasSameAct) {
				aProp = (obj.myEntity === actObj.myEntity) ? $tElem.attr(actObj.attr) : false;
			} else {
				aProp = aPropFlag ? $tElem.attr(actObj.attr) : false;
			}
			if (tAttrValue) {
				// ACT 只对第一次数据遍历
				if (tAttrValue === 'list') {
					tv = tv.split(",");
					$.each(tv, function (index, value) {
						t.doSetData(obj, actObj, formData, aProp, tn, value, 'list');
					})
				} else if (tAttrValue === 'string') {
					t.doSetData(obj, actObj, formData, aProp, tn, tv, 'string');
				} else {

				}
			} else if (obj.containNotAttr) {
				t.doSetData(obj, actObj, formData, aProp, tn, tv, 'string');
			}
			aPropFlag = false;
		}
	});
	for (var j = 0; j < myObjArr.length; j++) {
		var obj = myObjArr[j];
		$.each($elem.find('[' + obj.attr + ']'), function () {
			var $t = $(this);
			var tProp = $t.attr(obj.attr);
			if (tProp && tProp !== 'string' && tProp !== 'list') {
				t.doSetData(obj, actObj, formData, false, this.name, this.value, tProp);
			}
		});
	}
	return formData;
};

SillyFormData.prototype.doSetData = function (obj, actObj, formData, aProp, key, value, label) {
	var returnFlag = false;
	if (obj.myEntity === actObj.myEntity) {
		obj.count = obj.count > actObj.count ? obj.count : actObj.count;
		actObj.count = obj.count;
		returnFlag = true;
	}
	if (aProp) {
		var actIsList = actObj.myEntityIsList;
		var actEntity = actIsList ? actObj.myEntity + "[" + actObj.count++ + "]" : actObj.myEntity;
		if (actObj.myEntityKey) {
			formData.set(actEntity + actObj.myEntityKey, key);
			formData.set(actEntity + actObj.myEntityValue, actObj.valueFun(value));
			formData.set(actEntity + actObj.myEntityLabel, label);
			formData.set(actEntity + ".activitiHandler", aProp);
		} else {
			actEntity = actEntity ? (actEntity + ".") : "";
			formData.set(actEntity + key, actObj.valueFun(value));
		}
		if (returnFlag) {
			return;
		}
	}
	var isList = obj.myEntityIsList;
	var tempEntity = isList ? obj.myEntity + "[" + obj.count++ + "]" : obj.myEntity;
	if (obj.myEntityKey) {
		formData.set(tempEntity + obj.myEntityKey, key);
		formData.set(tempEntity + obj.myEntityValue, obj.valueFun(value));
		formData.set(tempEntity + obj.myEntityLabel, label);
	} else {
		tempEntity = tempEntity ? (tempEntity + ".") : "";
		formData.set(tempEntity + key, obj.valueFun(value));
	}
};