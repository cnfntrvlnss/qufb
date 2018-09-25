
/** init  nav
 *	menuName 	新增的菜单名称
 *	iconClass   新增菜单的图标类
 *	href	  	新增的菜单对应的URL地址,如果没有跳转地址则填写为"#"
 *	activeFlag 	新增的菜单项是否是打开状态，一般只默认打开一个菜单项，默认为 "N"
 *	treeviewAry 新增的菜单项的子菜单内容，格式为json数组
 *				[
 *					{
 *						"name":"用户管理",
 *						"href":"/user.html",
 *						"active": "N"
 *					}
 *				]
 */
function initNav(menuName, iconClass, href, activeFlag, treeviewAry) {

	var childHtml = "";
	childHtml = '<li class="';
	if (activeFlag == "Y") {
		childHtml += 'active menu-open ';
	}
	if (treeviewAry.length > 0) {
		childHtml += 'treeview">';
	} else {
		childHtml += '">';
	}

	childHtml += '<a href="' + href + '">';
	childHtml += '<i class="fa ' + iconClass + '"></i> <span>' + menuName
			+ '</span>';
	childHtml += '<span class="pull-right-container">';
	if (treeviewAry.length > 0) {
		childHtml += '<i class="fa fa-angle-left pull-right"></i>';
	}
	childHtml += '</span></a>';

	// 子菜单拼接
	if (treeviewAry.length > 0) {
		childHtml += '<ul class="treeview-menu">';

		for (var i = 0; i < treeviewAry.length; i++) {
			// 判断子菜单是否为打开状态
			if (treeviewAry[i]["active"] == "Y") {
				childHtml += '<li class="active"><a menu-controller="'
						+ treeviewAry[i]["url"]
						+ '" href="javascript:void(0)">';
			} else {
				childHtml += '<li><a menu-controller="' + treeviewAry[i]["url"]
						+ '" href="javascript:void(0)">';
			}

			childHtml += '<i class="fa fa-circle-o"></i>'
					+ treeviewAry[i]["name"] + '</a></li>';
		}
		childHtml += '</ul>';
	}

	childHtml += '</li>';
	$("#sideMenu").append(childHtml);
	
}

/**
 * 通过ajax请求控制主页面content层内容的刷新及跳转
 * url 		 ---- 跳转页面的地址
 * pageRenderparams --页面跳转时需要传递的参数
 */
function contentPageRender(url,pageRenderparams){
	
	$.ajax({
		url: url,
		type:"post",
		data:"",
		dataType:"html",
		async:true,
		success:function(data){
			$("#contentWrapper").html(data);	
			$("#pageRenderParams").val(pageRenderparams);
		},
		error:function(data){
			alert("ajax PageRender error!");
		}	
	});
	
}

/**
 * 必填非空校验
 * 
 */
function checkValRequired(domId){
	var val = document.getElementById(domId).value();
	var result = false;
	if(val == "" || val == null || val === undefined || val == undefined || val.length == 0 || !val.length){
		
	}else{
		result = true;
	}
	return result;
}

/**
 * 长度校验
 */
function checkValLength(val,length){
	
}

/**
 * 错误信息提示
 * mainInfoTitle - 错误信息标题
 * mainInfoText -- 错误信息详细内容
 */
function alertErrorInfo(mainInfoTitle,mainInfoText){
	
	$("#mainIndexInfoTipsTitle").text(mainInfoTitle);
	$("#mainIndexInfoTipsText").text(mainInfoText);
	$("#mianIndexInfoTips").modal("show");
}


