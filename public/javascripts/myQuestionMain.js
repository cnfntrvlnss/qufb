
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

 var menu={};
 menu.menuType=1;
$(function(){
 //初始化一级菜单
    initIndexNav();
    //initFirstIndex();
    //initSecondIndex(1);
});

    function initFirstIndex(){
         $.ajax({
             url: "/listMenu",
             dataType:"json",
             contentType : 'application/json;charset=UTF-8',
             type: 'POST',
             asnyc:false,
             data:JSON.stringify(menu), //转JSON字符串
             success: function(data){
                //循环初始化一级菜单
                $('#sideMenu').empty();
                $.each(data, function(i, e){
                    initNav(e.menuName, "fa-dashboard", e.menuUrl, "N", "");
                 });
              },
              error: function(data){
                    alert("error!!! " + data);
              }
         });
    }

    function initSecondIndex(parentMenuId){
             $.ajax({
                 url: "/listSubMenu/"+parentMenuId,
                 dataType:"json",
                   type: 'GET',
                   asnyc:false,
                 success: function(data){
                    //循环初始化一级菜单
                    $('#sideMenu').empty();
                    $.each(data, function(i, e){
                        initNav(e.menuName, "fa-dashboard", e.menuUrl, "N", "");
                     });
                  },
                  error: function(data){
                        alert("error!!! " + data);
                  }
             });
        }
    //初始化二级菜单
     function initIndexNav(){
            var treeAry = [
                    { "name":"问题列表", "url":"/myQuestionSubmit", "active": "N" },
                    { "name":"问题处理", "url":"/myQuestionDeal/75", "active": "N" ,"method":"GET"},
            ];
            initNav("问题管理","fa-dashboard","#","N",treeAry);
            initNav("项目管理","fa-dashboard","#","N",treeAry);
        }
    //获取所有的菜单
    function listMenu(){
         $.ajax({
             url: "/listSubMenu",
             dataType:"json",
             contentType : 'application/json;charset=UTF-8',
             type: 'POST',
             asnyc:false,
             data:JSON.stringify(menu), //转JSON字符串
             success: function(data){
                $('#menuId').empty();
                $.each(data, function(i, e){
                    $('#menuId').append();
                    $('#menuId').append('<ul>' +e.menuName+ '</ul>');
                 });
              },
              error: function(data){
                    alert("error!!! " + data);
              }
         });
        }