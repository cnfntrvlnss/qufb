        var selectParam={};
        var classCss;
         //得到查询的参数
        var queryParams = function() {
            var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                pageSize : this.pageSize, //页面大小
                pageNum : 1,//页码
                questionTitle:$("#questionTitle").val()
            };
            return temp;
        };
        loadTableData();
        var menu={};
        menu.menuType=1;
       // listMenu();

        //点击新建按钮
        $("#btnNew").click(function(){
            location.href = "myQuestionDeal/0";
        });
        //点击查询按钮
        $("#btnSelect").click(function(){
            if($("#questionTitle").val()!=null && $("#questionTitle").val()!=""){
                selectParam.questionTitle=$("#questionTitle").val();
            }else{
                delete selectParam.questionTitle;
            }
            if($("#questionDescription").val()!=null && $("#questionDescription").val()!=""){
                selectParam.questionDescription=$("#questionDescription").val();
            }else{
                delete selectParam.questionDescription;
            }
            if($("#feedbacker").val()!=0 && $("#feedbacker").val()!=""){
                selectParam.feedbacker=$("#feedbacker").val();
            }else{
                delete selectParam.feedbacker;
            }
            if($("#bugHeader").val()!=0 && $("#bugHeader").val()!=""){
                selectParam.bugHeader=$("#bugHeader").val();
            }else{
                delete selectParam.bugHeader;
            }
            if($("#transferName").val()!=0 && $("#transferName").val()!=""){
                selectParam.transferName=$("#transferName").val();
            }else{
                delete selectParam.transferName;
            }
            if($("#developerName").val()!=0 && $("#developerName").val()!=""){
                selectParam.developerName=$("#developerName").val();
            }else{
                delete selectParam.developerName;
            }
            if($("#schemeAuditName").val()!=0 && $("#schemeAuditName").val()!=""){
                selectParam.schemeAuditName=$("#schemeAuditName").val();
            }else{
                delete selectParam.schemeAuditName;
            }
            if($("#verifyName").val()!=0 && $("#verifyName").val()!=""){
                selectParam.verifyName=$("#verifyName").val();
            }else{
                delete selectParam.verifyName;
            }
            loadQuestionList();
        });

        //点击退出按钮
        $("#logOut").click(function(){
            logOut();
        });

        //问题列表查询功能
        function selectQuestion(){
            $.ajax({
                 url: "/listMyQuestion",
                 dataType:"json",
                 contentType : 'application/json;charset=UTF-8',
                 type: 'POST',
                 asnyc:false,
                 data:JSON.stringify(selectParam), //转JSON字符串
                 success: function(data){
                    $('#questionTable').empty();
                    $.each(data, function(i, e){
                        if(i % 2==0){
                            classCss="even";
                        }else{
                            classCss="odd";
                        }
                        $('#questionTable').append('<tr class='+classCss+'><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionId+'</a>'+ '</td><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionTitle+'</a>'+ '</td><td>' + e.bugHeader + '</td><td>' + e.questionDescription + '</td> <td>' + e.feedbackSuggestion+ '</td> <td>' + getMyDate(e.feedbackTime)+'</td><td>'  + e.flowStateName+'</td></tr>');
                     });
                  },
                  error: function(data){
                        alert("error!!! " + data);
                  }
             });
        }

        //对日期格式在前台格式化
        function getMyDate(str){
               var oDate = new Date(str),
               oYear = oDate.getFullYear(),
               oMonth = oDate.getMonth()+1,
               oDay = oDate.getDate(),
               oHour = oDate.getHours(),
               oMin = oDate.getMinutes(),
               oSen = oDate.getSeconds(),
               oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +' '+ getzf(oHour) +':'+ getzf(oMin) +':'+getzf(oSen);//最后拼接时间
               return oTime;
         };

        //对日期的月份加0处理
        function getzf(num){
             if(parseInt(num) < 10){
                 num = '0'+num;
             }
            return num;
         }


        //退出方法
        function logOut(){
            $.ajax({
                 url: "/logoutSubmit",
                 type: 'POST',
                 asnyc:false,
                 success: function(data){
                    location.href = "/login";
                  },
                  error: function(data){
                        alert("error!!! " + data);
                  }
             });
        }

        function loadTableData() {
            var table =   $("#questionTable").bootstrapTable({
                    striped:true,
                    pagination:true,
                    //sidePagination:'server',
                    pageSize:10,
                    pageNumber:1,
                    pageList:[10,20,30,40],
                    paginationPreText:'<',
                    paginationNextText:'>',
                    clickToSelect: true,
                    singleSelect: true,
                    maintainSelected:true,
                    search:false,
                    showRefresh:false,
                    searchOnEnterKey:true,
                    searchAlign:'left',
                    showSearchButton:true,
                    formatLoadingMessage:function(){
                        return "请稍等，正在加载中……";
                    },
                    url : "/listMyQuestion",
                    async:true,
                    method:'post',
                    queryParams : queryParams,//传递参数（*）
                    columns:[
                            {
                                field:'questionId',
                                align:'center',
                                formatter:function(value,row,index){
                                    return"<label><input type='radio'  id='questionIdCheck' value = '" + row.questionId + "' /><span class='lbl' id='questionIdCheck' value = '" + row.questionId + "'></span></label>";
                                }
                            },
                            {
                                field:'questionCode',
                                title:'问题编号',
                                align:'center',
                                formatter:function(value,row,index){
                                    return '<a onclick="questionUpdate('+row.questionId+');" >' + row.questionCode + '</a>';
                                }
                            },
                            {
                                field:'questionTitle',
                                title:'问题标题',
                                align:'center',
                                formatter:function(value,row,index){
                                    return '<a onclick="questionUpdate('+row.questionId+');" >' + row.questionTitle + '</a>';
                                }
                            },
                            {
                                field:'bugHeader',
                                title:'BUG负责人',
                                align:'center'
                            },
                            {
                                field:'questionDescription',
                                title:'问题描述',
                                align:'center'
                            },
                            {
                                field:'feedbackSuggestion',
                                title:'修改意见',
                                align:'center'
                            },
                            {
                                field:'feedbackTime',
                                title:'反馈时间',
                                align:'center',
                                formatter:function(value,row,index){
                                   return getMyDate(row.feedbackTime);
                                }
                            },
                            {
                                field:'flowStateName',
                                title:'流程状态',
                                align:'center'
                            }
                    ]
                });


            };

    function loadQuestionList(){
        $.ajax({
             url: "/listMyQuestion",
             dataType:"json",
             contentType : 'application/json;charset=UTF-8',
             type: 'POST',
             asnyc:false,
             data:JSON.stringify(selectParam), //转JSON字符串
             success: function(data){
                $("#questionTable").bootstrapTable('load', data);
              },
              error: function(data){
                    alert("error!!! " + data);
              }
         });
}
function questionAdd(){
	contentPageRender("/myQuestionDeal/0","")
}
    function questionDetail(){
       if($("#questionIdCheck:checked").length==0){
         layer.msg("请选择一个问题查看详情");
         return;
       } else{
            contentPageRender("/myQuestionInfo/"+$("#questionIdCheck:checked").val(),"")
       }
}
function questionUpdate(questionId){
	contentPageRender("/myQuestionDeal/"+questionId,questionId)
}