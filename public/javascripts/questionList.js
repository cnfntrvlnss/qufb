
        var classCss;
        $.ajax({
            url: "/listQuestion",
            success: function(data){
                $.each(data, function(i, e){
                    if(i % 2==0){
                        classCss="even";
                    }else{
                        classCss="odd";
                    }
                     $('#questionTable').append('<tr class='+classCss+'><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionId+'</a>'+ '</td><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionTitle+'</a>'+ '</td><td>' + e.bugHeader + '</td><td>' + e.questionDescription + '</td> <td>' + e.feedbackSuggestion+ '</td> <td>' + getMyDate(e.feedbackTime)+'</td><td>' + e.questionState+'</td><td>' + e.flowState+'</td></tr>');
                 });
            },
            error: function(data){
                alert("error!!! " + data);
            }
        });
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


