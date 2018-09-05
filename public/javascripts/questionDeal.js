        //获取一个问题信息
        var questionId=$("#questionId").val();
        $.ajax({
            url: "/getQuestionInfo/"+questionId,
           dataType:"json",
           type: 'GET',
           asnyc:false,
            success: function(data){
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
