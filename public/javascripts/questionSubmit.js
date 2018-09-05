        //保存一个问题
        var saveParam={};

        //点击保存按钮
        $("#btnSave").click(function(){
            saveParam.questionState=1;
            saveParam.flowState=1;
            save();
        });
        //点击提交按钮
        $("#btnSubmit").click(function(){
            saveParam.questionState=2;
            saveParam.flowState=2;
            save();
        });
        //保存方法
        function save(){
           if($("#questionTitle").val()!=null && $("#questionTitle").val()!=""){
                saveParam.questionTitle=$("#questionTitle").val();
           }else{
                alert("请填写问题标题");
                return;
           }
           if($("#bugHeader").val()!=null && $("#bugHeader").val()!=""){
                 saveParam.bugHeader=$("#bugHeader").val();
           }else{
                alert("请填写bug负责人");
                return ;
           }
           if($("#questionDescription").val()!=null && $("#questionDescription").val()!=""){
                 saveParam.questionDescription=$("#questionDescription").val();
           }else{
                alert("请填写问题描述");
                return;
           }
           if($("#feedbackSuggestion").val()!=null && $("#feedbackSuggestion").val()!=""){
                saveParam.feedbackSuggestion=$("#feedbackSuggestion").val();
           }
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