         var selectParam={};
         var classCss;
        selectQuestion();

        //点击新建按钮
        $("#btnNew").click(function(){
            location.href = "myQuestionDeal/0";
        })
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
            selectQuestion();

        })
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

                function getzf(num){
                     if(parseInt(num) < 10){
                         num = '0'+num;
                     }
                    return num;
                 }