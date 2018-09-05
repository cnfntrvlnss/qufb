        //获取一个问题信息
        var questionId=$("#questionId").val();
        $.ajax({
            url: "/getQuestionInfo/"+questionId,
           dataType:"json",
           type: 'GET',
           asnyc:false,
            success: function(data){
                //判断问题状态
                if(data.questionState==1){//提交问题
                    btnGroup1();
                }else if(data.questionState==2){
                    btnGroup2();//bug负责人处理问题
                }else if(data.questionState==3){
                    btnGroup3();//问题接口人处理问题
                }else if(data.questionState==4){
                    btnGroup4();//方案负责人处理问题
                }else if(data.questionState==5){
                    btnGroup5();//审核人处理问题
                }else if(data.questionState==6){
                    btnGroup6();//bug负责人审核问题
                }else if(data.questionState==7){
                    btnGroup7();//验证问题
                }

                //1
                $("#questionTitle").val(data.questionTitle);
                $("#bugHeader").val(data.bugHeader);
                $("#questionDescription").val(data.questionDescription);
                $("#feedbackSuggestion").val(data.feedbackSuggestion);
                //2
                $("#transferName").val(data.transferName);
                $("#auditSuggestion").val(data.auditSuggestion);
                //3
                $("#developerName").val(data.developerName);
                $("#transferSuggestion").val(data.transferSuggestion);
                //4
                $("#schemeAuditName").val(data.schemeAuditName);
                $("#solution").val(data.solution);
                //5
                $("#resultAuditName").val(data.resultAuditName);
                $("#schemeAuditSuggestion").val(data.schemeAuditSuggestion);
                //6
                $("#verifyName").val(data.verifyName);
                $("#resultAuditSuggestion").val(data.resultAuditSuggestion);
                //7
                $("#verifySuggestion").val(data.verifySuggestion);

            },
            error: function(data){
                alert("error!!! " + data);
            }
        });
        //对时间格式化处理
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
         //显示btngroup1
        function btnGroup1(){
            $("#btnGroup2").addClass("btnGroup");//2组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }
        //显示2组按钮
        function btnGroup2(){
            $("#btnGroup1").addClass("btnGroup");//1组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
         }
       //显示3组按钮
         function btnGroup3(){
             $("#btnGroup1").addClass("btnGroup");//1组隐藏
             $("#btnGroup2").addClass("btnGroup");//2组隐藏
             $("#btnGroup4").addClass("btnGroup");//4组隐藏
             $("#btnGroup5").addClass("btnGroup");//5组隐藏
             $("#btnGroup6").addClass("btnGroup");//6组隐藏
             $("#btnGroup7").addClass("btnGroup");//7组隐藏
          }
        //显示4组按钮
       function btnGroup4(){
           $("#btnGroup1").addClass("btnGroup");//1组隐藏
           $("#btnGroup2").addClass("btnGroup");//2组隐藏
           $("#btnGroup3").addClass("btnGroup");//3组隐藏
           $("#btnGroup5").addClass("btnGroup");//5组隐藏
           $("#btnGroup6").addClass("btnGroup");//6组隐藏
           $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }
         //显示5组按钮
       function btnGroup5(){
           $("#btnGroup1").addClass("btnGroup");//1组隐藏
           $("#btnGroup2").addClass("btnGroup");//2组隐藏
           $("#btnGroup3").addClass("btnGroup");//3组隐藏
           $("#btnGroup4").addClass("btnGroup");//4组隐藏
           $("#btnGroup6").addClass("btnGroup");//6组隐藏
           $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }
      //显示6组按钮
       function btnGroup6(){
           $("#btnGroup1").addClass("btnGroup");//1组隐藏
           $("#btnGroup2").addClass("btnGroup");//2组隐藏
           $("#btnGroup3").addClass("btnGroup");//3组隐藏
           $("#btnGroup4").addClass("btnGroup");//4组隐藏
           $("#btnGroup5").addClass("btnGroup");//5组隐藏
           $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }
       //显示7组按钮
       function btnGroup7(){
           $("#btnGroup1").addClass("btnGroup");//1组隐藏
           $("#btnGroup2").addClass("btnGroup");//2组隐藏
           $("#btnGroup3").addClass("btnGroup");//3组隐藏
           $("#btnGroup4").addClass("btnGroup");//4组隐藏
           $("#btnGroup5").addClass("btnGroup");//5组隐藏
           $("#btnGroup6").addClass("btnGroup");//6组隐藏
        }
        //提交问题者处理
        function save1(){

        }
        function submit1(){

        }
        //bug负责人处理问题
        function reject2(){

        }
        function submit2(){

        }
        //问题接口人处理问题
        function reject3(){

        }
        function submit3(){

        }
        //方案负责人处理
        function reject4(){

        }
        function submit4(){

        }

        //更新的ajax操作
        function updateQuestionInfo(){
            $.ajax({
                url: "/addQuestion",
               dataType:"json",
               contentType : 'application/json;charset=UTF-8',
               type: 'POST',
               asnyc:false,
               data:JSON.stringify(saveParam), //转JSON字符串
               success: function(data){
                    alert("保存成功");
                }
           });
        }