$(function() {
	
	$(".errorClass").each(function() {
		showError($(this));//遍历每个元素，使用每个元素来调用showError方法
	});

	$(".inputClass").focus(function() {
		var labelId = $(this).attr("id") + "Error";//通过输入框找到对应的label的id
		$("#" + labelId).text("");//把label的内容清空！
		showError($("#" + labelId));//隐藏没有信息的label
	});
	
	$(".inputClass").blur(
		function() {
			var id = $(this).attr("id");// 获取当前输入框的id
			var funName = "validate" + id.substring(0, 1).toUpperCase()
					+ id.substring(1) + "()";// 得到对应的校验函数名
			eval(funName);// 执行函数调用
	});
	
	$("#loginForm").submit(function() {
		var bool = true;//表示校验通过
		if(!validateUsername()) {
			bool = false;
		}
		/*if(!validatePassword()) {
			bool = false;
		}*/
		if(bool){
			return true;
		}
		return false;
	});
});


function validateUsername() {
	var id = "name";
	var value = $("#" + id).val();//获取输入框内容

	if(!value) {
		$("#" + id + "Error").text("账号不能为空！");
		showError($("#" + id + "Error"));
		return false;
	}
	return true;
}

function validatePassword() {
	var id = "password";
	var value = $("#" + id).val();//获取输入框内容

	if(!value) {
		$("#" + id + "Error").text("密码不能为空！");
		showError($("#" + id + "Error"));
		return false;
	}
	return true;
}


function showError(ele) {
	var text = ele.text();//获取元素的内容
	if(!text) {//如果没有内容
		ele.css("display", "none");//隐藏元素
	} else {//如果有内容
		ele.css("display", "");//显示元素
	}
}