

       /* $.ajax({
            url: "/listMyQuestion",
            dataType:"json",
            contentType : 'application/json;charset=UTF-8',
            /*type: 'GET',
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
                     $('#questionTable').append('<tr class='+classCss+'><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionId+'</a>'+ '</td><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionTitle+'</a>'+ '</td><td>' + e.bugHeader + '</td><td>' + e.questionDescription + '</td> <td>' + e.feedbackSuggestion+ '</td> <td>' + getMyDate(e.feedbackTime)+'</td><td>' + e.questionState+'</td><td>' + e.flowState+'</td></tr>');
                 });
            },
            error: function(data){
                alert("error!!! " + data);
            }
        });*/



