        //加载下拉框的内容
        var questionId=$("#questionId").val();
        var updateParam={};//参数对象
        var listAllUserParam={};
        listAllUserParam.departmentId=1;
        listAllUserParam.unitId=0;
       listAllUser();


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
                            listDepartment(1);
                           // listAllUser(1);
                        }else if(data.operateFlag==2){
                            btnGroup2();//bug负责人处理问题
                            listDepartment(2);
                            //listAllUser(2);
                        }else if(data.operateFlag==3){
                            btnGroup3();//问题接口人处理问题
                             listDepartment(3);
                             //listAllUser(3);
                        }else if(data.operateFlag==4){
                            btnGroup4();//方案负责人处理问题
                            listDepartment(4);
                            //listAllUser(4);
                        }else if(data.operateFlag==5){
                            btnGroup5();//审核人处理问题
                            listDepartment(5);
                            //listAllUser(5);
                        }else if(data.operateFlag==6){
                            btnGroup6();//bug负责人审核问题
                            listDepartment(6);
                            //listAllUser(6);
                        }else if(data.operateFlag==7){
                            btnGroup7();//验证问题
                            //listAllUser(7);
                        }else{//只是展示信息，隐藏所有的button
                            btnGroup8();//隐藏全部按钮
                            //listAllUser(8);
                        }
                        //1
                        $("#questionTitle").val(data.questionTitle);
                        $("#bugHeader").val(data.bugHeaderId);
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
                        return;
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
            if($("#bugHeader").val() != null && $("#bugHeader").val() != "" && $("#bugHeader").val() != 0){
                 updateParam.bugHeader=$("#bugHeader").val();
            }else{
                layer.msg("请选择bug负责人");
                return ;
            }
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
             if($("#transferName").val() == "" || $("#transferName").val() == null || $("#transferName").val() == 0){
                    layer.msg("请选择问题接口人");
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
            if($("#developerName").val()=="" || $("#developerName").val() ==null || $("#developerName").val() ==0){
                   layer.msg("请选择方案责任人");
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
            if($("#schemeAuditName").val() == "" || $("#schemeAuditName").val() == null || $("#schemeAuditName").val() == 0){
                   layer.msg("请选择方案审核人");
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
            if($("#resultAuditName").val()== "" || $("#resultAuditName").val() == null || $("#resultAuditName").val() == 0){
                   layer.msg("请选择方案审核人");
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
            if($("#verifyName").val()== "" || $("#verifyName").val() == null ||  $("#verifyName").val() == 0){
                   layer.msg("请选择验证人员");
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
         var listUserParam={};
        //一级部门更改，触发联动二级部门和用户
        $("#department1").change(function(){
            if($("#department1").val()!=0){
                listUnit(1);
                listUserParam.departmentId=$("#department1").val();
                listUserParam.unitId=0;
                listUser(1);
            }else if($("#unit1").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit1").val();
                listUser(1);
            }else{
                $("#unit1").html("");
                $("#bugHeader").html("");
            }
        })
        //二级部门更改，触发联动用户
        $("#unit1").change(function(){
            if($("#unit1").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit1").val();
                listUser(1);
            }else if($("#department1").val()!=0){
                listUserParam.departmentId=$("#department1").val();
                listUserParam.unitId=0;
                listUser(1);
            }else{
                $("#unit1").html("");
                $("#bugHeader").html("");
            }
        })

        //一级部门更改，触发联动二级部门和用户
        $("#department2").change(function(){
            if($("#department2").val()!=0){
                listUnit(2);
                listUserParam.departmentId=$("#department2").val();
                listUserParam.unitId=0;
                listUser(2);
            }else if($("#unit2").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit2").val();
                listUser(2);
            }else{
                $("#unit2").html("");
                $("#transferName").html("");
            }
        })
        //二级部门更改，触发联动用户
        $("#unit2").change(function(){
            if($("#unit2").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit2").val();
                listUser(2);
            }else if($("#department2").val()!=0){
                listUserParam.departmentId=$("#department2").val();
                listUserParam.unitId=0;
                listUser(2);
            }else{
                $("#unit2").html("");
                $("#transferName").html("");
            }
        })

         //一级部门更改，触发联动二级部门和用户
        $("#department3").change(function(){
            if($("#department3").val()!=0){
                listUnit(3);
                listUserParam.departmentId=$("#department3").val();
                listUserParam.unitId=0;
                listUser(3);
            }else if($("#unit3").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit3").val();
                listUser(3);
            }else{
                $("#unit3").html("");
                $("#developerName").html("");
            }
        })
        //二级部门更改，触发联动用户
        $("#unit3").change(function(){
           if($("#unit3").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit3").val();
                listUser(3);
            }else if($("#department3").val()!=0){
                listUserParam.departmentId=$("#department3").val();
                listUserParam.unitId=0;
                listUser(3);
            }else{
                $("#unit3").html("");
                $("#developerName").html("");
            }
        })

         //一级部门更改，触发联动二级部门和用户
        $("#department4").change(function(){
            if($("#department4").val()!=0){
                listUnit(4);
                listUserParam.departmentId=$("#department4").val();
                listUserParam.unitId=0;
                listUser(4);
            }else if($("#unit4").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit4").val();
                listUser(4);
            }else{
                $("#unit4").html("");
                $("#schemeAuditName").html("");
            }
        })
        //二级部门更改，触发联动用户
        $("#unit4").change(function(){
            if($("#unit4").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit4").val();
                listUser(4);
            }else if($("#department4").val()!=0){
                listUserParam.departmentId=$("#department4").val();
                listUserParam.unitId=0;
                listUser(4);
            }else{
                $("#unit4").html("");
                $("#schemeAuditName").html("");
            }
        })

         //一级部门更改，触发联动二级部门和用户
        $("#department5").change(function(){
            if($("#department5").val()!=0){
                listUnit(5);
                listUserParam.departmentId=$("#department5").val();
                listUserParam.unitId=0;
                listUser(5);
            }else if($("#unit5").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit5").val();
                listUser(5);
            }else{
                $("#unit5").html("");
                $("#resultAuditName").html("");
            }
        })
        //二级部门更改，触发联动用户
        $("#unit5").change(function(){
            if($("#unit5").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit5").val();
                listUser(5);
            }else if($("#department5").val()!=0){
                listUserParam.departmentId=$("#department5").val();
                listUserParam.unitId=0;
                listUser(5);
            }else{
                $("#unit5").html("");
                $("#resultAuditName").html("");
            }
        })
 //一级部门更改，触发联动二级部门和用户
        $("#department6").change(function(){
            if($("#department6").val()!=0){
                listUnit(6);
                listUserParam.departmentId=$("#department6").val();
                listUserParam.unitId=0;
                listUser(6);
            }else if($("#unit6").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit6").val();
                listUser(6);
            }else{
                $("#unit6").html("");
                $("#verifyName").html("");
            }
        })
        //二级部门更改，触发联动用户
        $("#unit6").change(function(){
            if($("#unit6").val()!=0){
                listUserParam.departmentId=0;
                listUserParam.unitId=$("#unit6").val();
                listUser(6);
            }else if($("#department6").val()!=0){
                listUserParam.departmentId=$("#department6").val();
                listUserParam.unitId=0;
                listUser(6);
            }else{
                $("#unit6").html("");
                $("#verifyName").html("");
            }
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
                 updateParam.bugHeaderId=$("#bugHeader").val();
                 updateParam.bugHeader=$("#bugHeader option:selected").text();
            }
            /*else{
                layer.msg("请填写bug负责人");
                return ;
            }*/
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
            updateParam.transferName=$("#transferName option:selected").text();
            updateParam.transferId=$("#transferName").val();
            updateQuestionInfo();
        }

        //问题接口人处理问题
        function save3(){
            updateParam.transferSuggestion=$("#transferSuggestion").val();//审核意见
            updateParam.developerName=$("#developerName option:selected").text();
            updateParam.developerId=$("#developerName").val();
            updateQuestionInfo();
        }

        //方案负责人处理
        function save4(){
            updateParam.solution=$("#solution").val();//审核意见
            updateParam.schemeAuditName=$("#schemeAuditName option:selected").text();
            updateParam.schemeAuditId=$("#schemeAuditName").val();
            updateQuestionInfo();
        }

        //方案审核人处理问题
        function save5(){
            updateParam.resultAuditId=$("#resultAuditName").val();//审核意见
             updateParam.resultAuditName=$("#resultAuditName option:selected").text();//审核意见
            updateParam.schemeAuditSuggestion=$("#schemeAuditSuggestion").val();
            updateQuestionInfo();
        }

        //bug负责人处理问题
        function save6(){
            updateParam.verifyId=$("#verifyName").val();//审核意见
            updateParam.verifyName=$("#verifyName option:selected").text();//审核意见
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
            $("#questionDescription").attr("disabled","true");
            $("#questionDescription").css("backgroundColor","#f0f0f0");
            $("#feedbackSuggestion").attr("disabled","true");
            $("#feedbackSuggestion").css("backgroundColor","#f0f0f0");
            $("#bugHeader").attr("disabled","true");
            $("#bugHeader").css("backgroundColor","#f0f0f0");
            $("#departmentDiv1").css("display","none");//隐藏div
            $("#unitDiv1").css("display","none");//隐藏div
       }

        //disable divGroup2
        function divGroup2(){
            $("#transferName").attr("disabled","true");
            $("#auditSuggestion").attr("disabled","true");
            $("#departmentDiv2").css("display","none");//隐藏div
            $("#unitDiv2").css("display","none");//隐藏div
       }

        //disable divGroup3
        function divGroup3(){
            $("#developerName").attr("disabled","true");
            $("#transferSuggestion").attr("disabled","true");
            $("#departmentDiv3").css("display","none");//隐藏div
            $("#unitDiv3").css("display","none");//隐藏div
        }

        //disable divGroup4
        function divGroup4(){
            $("#schemeAuditName").attr("disabled","true");
            $("#solution").attr("disabled","true");
            $("#departmentDiv4").css("display","none");//隐藏div
            $("#unitDiv4").css("display","none");//隐藏div
       }

        //disable divGroup5
        function divGroup5(){
            $("#resultAuditName").attr("disabled","true");
            $("#schemeAuditSuggestion").attr("disabled","true");
            $("#departmentDiv5").css("display","none");//隐藏div
            $("#unitDiv5").css("display","none");//隐藏div
        }

        //disable divGroup6
        function divGroup6(){
            $("#verifyName").attr("disabled","true");
            $("#resultAuditSuggestion").attr("disabled","true");
            $("#departmentDiv6").css("display","none");//隐藏div
            $("#unitDiv6").css("display","none");//隐藏div
       }

        //disable divGroup7
        function divGroup7(){
            $("#verifySuggestion").attr("disabled","true");
        }
        //====================================获取下拉框的内容====================================================
        function listDepartment(department){
             $.ajax({
                  url: "/listDepartment",
                  dataType:"json",
                  contentType : 'application/json;charset=UTF-8',
                  type: 'GET',
                  asnyc:false,
                  success: function (result) {
                  if(department==1){
                        $("#department1").html("");
                        $("#department1").append("<option value='0'>请选择一级部门</option>");
                        for(var i=0;i<result.length;i++){
                            $("#department1").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                        }
                  }else if(department==2){
                        $("#department2").html("");
                        $("#department2").append("<option value='0'>请选择一级部门</option>");
                        for(var i=0;i<result.length;i++){
                            $("#department2").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                        }
                  }else if(department==3){
                          $("#department3").html("");
                          $("#department3").append("<option value='0'>请选择一级部门</option>");
                          for(var i=0;i<result.length;i++){
                              $("#department3").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                          }
                    }else if(department==4){
                           $("#department4").html("");
                           $("#department4").append("<option value='0'>请选择一级部门</option>");
                           for(var i=0;i<result.length;i++){
                               $("#department4").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                           }
                     }else if(department==5){
                            $("#department5").html("");
                            $("#department5").append("<option value='0'>请选择一级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#department5").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                      }else if(department==6){
                           $("#department6").html("");
                           $("#department6").append("<option value='0'>请选择一级部门</option>");
                           for(var i=0;i<result.length;i++){
                               $("#department6").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                           }
                     }

                   },
                    error:function(){
                    }
               });

        }
        //获取二级部门
        function listUnit(unitType){
             $.ajax({
                  url: "/listUnit/"+$("#department"+unitType).val(),
                  dataType:"json",
                  contentType : 'application/json;charset=UTF-8',
                  type: 'GET',
                  asnyc:false,
                  success: function (result) {
                   if(result.length!=0){
                       if(unitType == 1){
                            $("#unit1").html("");
                            $("#unit1").append("<option value='0'>请选择二级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#unit1").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                       }else if(unitType == 2){
                            $("#unit2").html("");
                            $("#unit2").append("<option value='0'>请选择二级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#unit2").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                       }else if(unitType == 3){
                            $("#unit3").html("");
                            $("#unit3").append("<option value='0'>请选择二级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#unit3").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                       }else if(unitType == 4){
                            $("#unit4").html("");
                            $("#unit4").append("<option value='0'>请选择二级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#unit4").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                       }else if(unitType == 5){
                            $("#unit5").html("");
                            $("#unit5").append("<option value='0'>请选择二级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#unit5").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                       }else if(unitType == 6){
                            $("#unit6").html("");
                            $("#unit6").append("<option value='0'>请选择二级部门</option>");
                            for(var i=0;i<result.length;i++){
                                $("#unit6").append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                            }
                       }
                       }else{
                             layer.msg("该部门下还没有二级部门，请选择其他部门或在部门管理页面添加相应的二级部门。");
                       }
                   },
                    error:function(){
                    }
               });
        }
        //获取所有的用户列表
        function listUser(userType){
            if(listUserParam.departmentId == 0 && listUserParam.unitId == 0){//一级部门及二级部门都是0，则不执行任何操作
                   layer.msg("请选择部门");
            }
             $.ajax({
                  url: "/listUser/",
                  dataType:"json",
                    contentType : 'application/json;charset=UTF-8',
                    type: 'GET',
                    asnyc:false,
                    data:listUserParam, //转JSON字符串
                    success: function (result) {
                        if(result.length != 0){
                            if(userType == 1){
                                $("#bugHeader").html("");
                                for(var i=0;i<result.length;i++){
                                    $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                }
                            }else if(userType == 2 ){
                                $("#transferName").html("");
                                for(var i=0;i<result.length;i++){
                                    $("#transferName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                }
                            }else if(userType == 3 ){
                                 $("#developerName").html("");
                                 for(var i=0;i<result.length;i++){
                                     $("#developerName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                 }
                             }else if(userType == 4 ){
                                   $("#schemeAuditName").html("");
                                   for(var i=0;i<result.length;i++){
                                       $("#schemeAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                   }
                             }else if(userType == 5 ){
                                   $("#resultAuditName").html("");
                                   for(var i=0;i<result.length;i++){
                                       $("#resultAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                   }
                             }else if(userType == 6 ){
                                 $("#verifyName").html("");
                                 for(var i=0;i<result.length;i++){
                                     $("#verifyName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                 }
                            }
                        }else{
                            layer.msg("该部门下还没有用户，请选择其他部门或在用户管理页面添加相应的用户。");
                        }
                    },
                    error:function(){
                    }
               });
        }


          //获取所有的用户列表

            function listAllUser(allUserType){
                 $.ajax({
                      url: "/listUser/",
                      dataType:"json",
                        contentType : 'application/json;charset=UTF-8',
                        type: 'GET',
                        asnyc:false,
                        data:listAllUserParam, //转JSON字符串
                        success: function (result) {
                            if(result.length != 0){
                                if(allUserType == 1){
                                    $("#bugHeader").html("");
                                     for(var i=0;i<result.length;i++){
                                        $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                    }
                                }else if(allUserType == 2){
                                    $("#bugHeader").html("");
                                    $("#transferName").html("");
                                     for(var i=0;i<result.length;i++){
                                        $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                        $("#transferName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                    }
                                }else if(allUserType == 3){
                                     $("#bugHeader").html("");
                                     $("#transferName").html("");
                                     $("#developerName").html("");
                                      for(var i=0;i<result.length;i++){
                                         $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                         $("#transferName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                         $("#developerName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                     }
                                 }else if(allUserType == 4){
                                       $("#bugHeader").html("");
                                       $("#transferName").html("");
                                       $("#developerName").html("");
                                       $("#schemeAuditName").html("");
                                        for(var i=0;i<result.length;i++){
                                           $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#transferName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#developerName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#schemeAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                       }
                                   }else if(allUserType == 5){
                                       $("#bugHeader").html("");
                                       $("#transferName").html("");
                                       $("#developerName").html("");
                                       $("#schemeAuditName").html("");
                                       $("#resultAuditName").html("");
                                        for(var i=0;i<result.length;i++){
                                           $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#transferName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#developerName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#schemeAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#resultAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                       }
                                   }else{
                                       $("#bugHeader").html("");
                                       $("#transferName").html("");
                                       $("#developerName").html("");
                                       $("#schemeAuditName").html("");
                                       $("#resultAuditName").html("");
                                       $("#verifyName").html("");
                                       $("#bugHeader").append("<option value='0'>请选择</option>");
                                       $("#transferName").append("<option value='0'>请选择</option>");
                                       $("#developerName").append("<option value='0'>请选择</option>");
                                       $("#schemeAuditName").append("<option value='0'>请选择</option>");
                                       $("#resultAuditName").append("<option value='0'>请选择</option>");
                                       $("#verifyName").append("<option value='0'>请选择</option>");

                                        for(var i=0;i<result.length;i++){
                                           $("#bugHeader").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#transferName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#developerName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#schemeAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#resultAuditName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                           $("#verifyName").append("<option value='"+result[i].userId+"'>"+result[i].name+"</option>");
                                       }
                                   }

                            }else{
                                layer.msg("该部门下还没有用户，请选择其他部门或在用户管理页面添加相应的用户。");
                            }

                            //判断是否调用问题的基本信息
                            //若问题id为0，则表示从“新建”进入该页面，否则是从超链接进入该页面
                            if(questionId != 0 && questionId != "" && questionId != null){
                                updateParam.questionId = questionId;
                                getQuestionInfo();
                            }else{
                               btnGroup1();//显示第一组按钮，提供保存和提交的操作。
                               listDepartment(1);
                            }
                        },
                        error:function(){
                        }
                   });
            }