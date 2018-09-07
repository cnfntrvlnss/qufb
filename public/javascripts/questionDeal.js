        var questionId=$("#questionId").val();
        var updateParam={};//参数对象

        //若问题id为0，则表示从“新建”进入该页面，否则是从超链接进入该页面
        if(questionId != 0 && questionId != "" && questionId != null){
            updateParam.questionId = questionId;
            getQuestionInfo();
        }else{
            btnGroup1();//显示第一组按钮，提供保存和提交的操作。
        }



        //=====================实际的ajax方法==========================================================================
        //获取一个问题信息
        function getQuestionInfo(){
             $.ajax({
                   url: "/getQuestionInfo/"+questionId,
                   dataType:"json",
                   type: 'GET',
                   asnyc:false,
                   success: function(data){
                        //判断问题状态
                        if(data.operateFlag==1){//提交问题
                            btnGroup1();
                        }else if(data.operateFlag==2){
                            btnGroup2();//bug负责人处理问题
                        }else if(data.operateFlag==3){
                            btnGroup3();//问题接口人处理问题
                        }else if(data.operateFlag==4){
                            btnGroup4();//方案负责人处理问题
                        }else if(data.operateFlag==5){
                            btnGroup5();//审核人处理问题
                        }else if(data.operateFlag==6){
                            btnGroup6();//bug负责人审核问题
                        }else if(data.operateFlag==7){
                            btnGroup7();//验证问题
                        }else{//只是展示信息，隐藏所有的button
                            btnGroup8();//隐藏全部按钮
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
                        //5将bug负责人默认到结果审核人处
                        if(data.resultAuditName== null || data.resultAuditName == ""){
                             $("#resultAuditName").val(data.bugHeader);
                        }else{
                             $("#resultAuditName").val(data.resultAuditName);
                        }
                        $("#schemeAuditSuggestion").val(data.schemeAuditSuggestion);
                        //6将提出问题者默认到验证人员处
                        if(data.verifyName== null || data.verifyName == ""){
                             $("#verifyName").val(data.feedbacker);
                        }else{
                             $("#verifyName").val(data.verifyName);
                        }
                        $("#resultAuditSuggestion").val(data.resultAuditSuggestion);
                        //7
                        $("#verifySuggestion").val(data.verifySuggestion);
                    },
                    error: function(data){
                        alert("获取问题信息失败，请及时联系管理员解决问题 " );
                    }
                });
        }

        //更新一个问题信息
        function updateQuestionInfo(){
            $.ajax({
                url: "/updateQuestionInfo",
               dataType:"json",
               contentType : 'application/json;charset=UTF-8',
               type: 'POST',
               asnyc:false,
               data:JSON.stringify(updateParam), //转JSON字符串
               success: function(data){
                   layer.msg("操作成功");
                    location.href = "/myQuestionSubmit";
                }
           });
        }

         //添加一个问题
        function addQuestionInfo(operateType){
            $.ajax({
              url: "/addQuestion",
              dataType:"json",
              contentType : 'application/json;charset=UTF-8',
              type: 'POST',
              asnyc:false,
              data:JSON.stringify(updateParam), //转JSON字符串
              success: function(data){
                if(operateType==1){
                      layer.msg("保存成功");
                }else if(operateType==2){
                      layer.msg("提交成功");
                }
                location.href = "/myQuestionSubmit";//定位到问题列表的页面
               }
           });
        }


        //======================按钮的点击操作=========================================================================
        var operateType;//为了区别保存和提交的操作类型：1：保存、2：提交

        //点击保存
        $("#btnSave1").click(function(){
            operateType=1;//保存操作
            updateParam.submitType=0;//第一个节点保存
            save1();
        })

        //点击提交
        $("#btnSubmit1").click(function(){
            operateType=2;//提交操作
            updateParam.submitType=1;//第一个节点提交
            save1();
        })

        //bug负责人点击驳回
        $("#btnReject2").click(function(){
            if($("#auditSuggestion").val() == "" || $("#auditSuggestion").val() == null){
               layer.msg("请填写驳回意见");
               return;
            }
            updateParam.submitType=2;//第二个节点驳回
            save2();
        })

        //bug负责人点击提交
        $("#btnSubmit2").click(function(){
             if($("#transferName").val() == "" || $("#transferName").val() == null){
                    layer.msg("请填写问题接口人");
                    return;
             }
             updateParam.submitType=3;//第二个节点提交
             save2();
        })

        //问题接口人点击驳回
        $("#btnReject3").click(function(){
            if($("#transferSuggestion").val()=="" || $("#transferSuggestion").val() ==null){
                   layer.msg("请填写驳回意见");
                   return;
             }
            updateParam.submitType=4;//第三个节点驳回
            save3();
        })

        //问题接口人点击提交
        $("#btnSubmit3").click(function(){
            if($("#developerName").val()=="" || $("#developerName").val() ==null){
                   layer.msg("请填写方案责任人");
                   return;
             }
            updateParam.submitType=5;//第三个节点提交
            save3();
        })

        //方案责任人点击驳回
        $("#btnReject4").click(function(){
            if($("#solution").val() == "" || $("#solution").val() == null){
                   layer.msg("请填写驳回意见");
                   return;
             }
           updateParam.submitType=6;//第四个节点驳回
            save4();
        })

        //方案责任人点击提交
        $("#btnSubmit4").click(function(){
            if($("#schemeAuditName").val()=="" || $("#schemeAuditName").val() ==null){
                   layer.msg("请填写方案审核人");
                   return;
            }
            if($("#solution").val()=="" || $("#solution").val() ==null){
                   layer.msg("请填写解决方案");
                   return;
             }
           updateParam.submitType=7;//第四个节点提交
            save4();
        })

        //方案审核人点击驳回
        $("#btnReject5").click(function(){
            if($("#schemeAuditSuggestion").val() == "" || $("#schemeAuditSuggestion").val() == null){
                   layer.msg("请填写驳回意见");
                   return;
             }
            updateParam.submitType=8;//第五个节点驳回
            save5();
        })

        //方案审核人点击提交
        $("#btnSubmit5").click(function(){
            if($("#schemeAuditName").val()=="" || $("#schemeAuditName").val() ==null){
                   layer.msg("请填写方案审核人");
                   return;
            }
            updateParam.submitType=9;//第五个节点提交
            save5();
        })

        //bug负责人点击驳回
        $("#btnReject6").click(function(){
             if($("#resultAuditSuggestion").val() == "" || $("#resultAuditSuggestion").val() == null){
                   layer.msg("请填写驳回意见");
                   return;
             }
            updateParam.submitType=10;//第六个节点驳回
            save6();
        })

        //bug负责人点击提交
        $("#btnSubmit6").click(function(){
            if($("#verifyName").val()=="" || $("#verifyName").val() ==null){
                   layer.msg("请填写验证人员");
                   return;
            }
            updateParam.submitType=11;//第六个节点提交
            save6();
        })

        //验证人点击驳回
        $("#btnReject7").click(function(){
            if($("#verifySuggestion").val() == "" || $("#verifySuggestion").val() == null){
                   layer.msg("请填写驳回意见");
                   return;
             }
            updateParam.submitType=12;//第七个节点驳回
            save7();
        })

        //验证人点击关闭问题
        $("#btnSubmit7").click(function(){
            updateParam.submitType=13;//第七个节点关闭问题
            save7();
        })


        //============================业务逻辑处理=======================================================================

        //提交问题者处理
        function save1(){
            if($("#questionTitle").val()!=null && $("#questionTitle").val()!=""){
                updateParam.questionTitle=$("#questionTitle").val();
            }else{
                layer.msg("请填写问题标题");
                return;
            }
            if($("#bugHeader").val()!=null && $("#bugHeader").val()!=""){
                 updateParam.bugHeader=$("#bugHeader").val();
            }else{
                layer.msg("请填写bug负责人");
                return ;
            }
            if($("#questionDescription").val()!=null && $("#questionDescription").val()!=""){
                 updateParam.questionDescription=$("#questionDescription").val();
            }else{
                layer.msg("请填写问题描述");
                return;
            }
            if($("#feedbackSuggestion").val()!=null && $("#feedbackSuggestion").val()!=""){
                updateParam.feedbackSuggestion=$("#feedbackSuggestion").val();
            }
            if($("#questionId").val() == 0 || $("#questionId").val() == "" || $("#questionId").val() == null){//一次保存or提交
                addQuestionInfo(operateType);
            }else{//更新
                updateQuestionInfo();
            }
        }

        //bug负责人处理问题
        function save2(){
            updateParam.auditSuggestion=$("#auditSuggestion").val();//审核意见
            updateParam.transferName=$("#transferName").val();
            updateQuestionInfo();
        }

        //问题接口人处理问题
        function save3(){
            updateParam.transferSuggestion=$("#transferSuggestion").val();//审核意见
            updateParam.developerName=$("#developerName").val();
            updateQuestionInfo();
        }

        //方案负责人处理
        function save4(){
            updateParam.solution=$("#solution").val();//审核意见
            updateParam.schemeAuditName=$("#schemeAuditName").val();
            updateQuestionInfo();
        }

        //方案审核人处理问题
        function save5(){
            updateParam.resultAuditName=$("#resultAuditName").val();//审核意见
            updateParam.schemeAuditSuggestion=$("#schemeAuditSuggestion").val();
            updateQuestionInfo();
        }

        //bug负责人处理问题
        function save6(){
            updateParam.verifyName=$("#verifyName").val();//审核意见
            updateParam.resultAuditSuggestion=$("#resultAuditSuggestion").val();
            updateQuestionInfo();
           }

        //验证人员处理问题
        function save7(){
           updateParam.verifySuggestion=$("#verifySuggestion").val();//审核意见
           updateQuestionInfo();
       }

        //========================按钮的显隐性及不可编辑的改变=========================================================
        //显示btngroup1
        function btnGroup1(){
            divGroup2();
            divGroup3();
            divGroup4();
            divGroup5();
            divGroup6();
            divGroup7();

            $("#btnGroup2").addClass("btnGroup");//2组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }

        //显示2组按钮
        function btnGroup2(){
            divGroup1();
            divGroup3();
            divGroup4();
            divGroup5();
            divGroup6();
            divGroup7();
            $("#btnGroup1").addClass("btnGroup");//1组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
         }

        //显示3组按钮
        function btnGroup3(){
             divGroup1();
             divGroup2();
             divGroup4();
             divGroup5();
             divGroup6();
             divGroup7();
             $("#btnGroup1").addClass("btnGroup");//1组隐藏
             $("#btnGroup2").addClass("btnGroup");//2组隐藏
             $("#btnGroup4").addClass("btnGroup");//4组隐藏
             $("#btnGroup5").addClass("btnGroup");//5组隐藏
             $("#btnGroup6").addClass("btnGroup");//6组隐藏
             $("#btnGroup7").addClass("btnGroup");//7组隐藏
       }

        //显示4组按钮
        function btnGroup4(){
            divGroup1();
            divGroup2();
            divGroup3();
            divGroup5();
            divGroup6();
            divGroup7();
            $("#btnGroup1").addClass("btnGroup");//1组隐藏
            $("#btnGroup2").addClass("btnGroup");//2组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }

        //显示5组按钮
        function btnGroup5(){
            divGroup1();
            divGroup2();
            divGroup3();
            divGroup4();
            divGroup6();
            divGroup7();
            $("#btnGroup1").addClass("btnGroup");//1组隐藏
            $("#btnGroup2").addClass("btnGroup");//2组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }

        //显示6组按钮
        function btnGroup6(){
            divGroup1();
            divGroup2();
            divGroup3();
            divGroup4();
            divGroup5();
            divGroup7();
            $("#btnGroup1").addClass("btnGroup");//1组隐藏
            $("#btnGroup2").addClass("btnGroup");//2组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }

        //显示7组按钮
        function btnGroup7(){
            divGroup1();
            divGroup2();
            divGroup3();
            divGroup4();
            divGroup5();
            divGroup6();
            $("#btnGroup1").addClass("btnGroup");//1组隐藏
            $("#btnGroup2").addClass("btnGroup");//2组隐藏
            $("#btnGroup3").addClass("btnGroup");//3组隐藏
            $("#btnGroup4").addClass("btnGroup");//4组隐藏
            $("#btnGroup5").addClass("btnGroup");//5组隐藏
            $("#btnGroup6").addClass("btnGroup");//6组隐藏
        }

        //隐藏7组按钮
        function btnGroup8(){
           divGroup1();
           divGroup2();
           divGroup3();
           divGroup4();
           divGroup5();
           divGroup6();
           divGroup7();
           $("#btnGroup1").addClass("btnGroup");//1组隐藏
           $("#btnGroup2").addClass("btnGroup");//2组隐藏
           $("#btnGroup3").addClass("btnGroup");//3组隐藏
           $("#btnGroup4").addClass("btnGroup");//4组隐藏
           $("#btnGroup5").addClass("btnGroup");//5组隐藏
           $("#btnGroup6").addClass("btnGroup");//6组隐藏
           $("#btnGroup7").addClass("btnGroup");//7组隐藏
        }

        //disable divGroup1
        function divGroup1(){
            $("#questionTitle").attr("disabled","true");
            $("#questionTitle").css("backgroundColor","#f0f0f0");
            $("#bugHeader").attr("disabled","true");
            $("#bugHeader").css("backgroundColor","#f0f0f0");
            $("#questionDescription").attr("disabled","true");
            $("#questionDescription").css("backgroundColor","#f0f0f0");
            $("#feedbackSuggestion").attr("disabled","true");
            $("#feedbackSuggestion").css("backgroundColor","#f0f0f0");
       }

        //disable divGroup2
        function divGroup2(){
            $("#transferName").attr("disabled","true");
            $("#auditSuggestion").attr("disabled","true");
       }

        //disable divGroup3
        function divGroup3(){
            $("#developerName").attr("disabled","true");
            $("#transferSuggestion").attr("disabled","true");
        }

        //disable divGroup4
        function divGroup4(){
            $("#schemeAuditName").attr("disabled","true");
            $("#solution").attr("disabled","true");
       }

        //disable divGroup5
        function divGroup5(){
            $("#resultAuditName").attr("disabled","true");
            $("#schemeAuditSuggestion").attr("disabled","true");
        }

        //disable divGroup6
        function divGroup6(){
            $("#verifyName").attr("disabled","true");
            $("#resultAuditSuggestion").attr("disabled","true");
       }

        //disable divGroup7
        function divGroup7(){
            $("#verifySuggestion").attr("disabled","true");
        }

