
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
                     $('#questionTable').append('<tr class='+classCss+'><td>' + '<a href="myQuestionDeal/'+e.questionId+'">'+e.questionId+'</a>'+ '</td><td>' + e.questionTitle+ '</td><td>' + e.bugHeader + '</td><td>' + e.questionDescription + '</td> <td>' + e.feedbackSuggestion+ '</td> <td>' + getMyDate(e.feedbackTime)+'</td><td>' + e.questionState+'</td><td>' + e.flowState+'</td></tr>');
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


        		/*	$('#questionTable').datagrid({
        				url: "/listQuestion",
        				scrollBarSize: 20,
        				loadMsg: "加载中......",
        				singleSelect: true,
        				checkOnSelect: false,
        				selectOnCheck: false,
        				idField: 'rowNumber',
        				fit: true,
        				pageNumber: 1,
        				pageSize: 20,
        				pageList: [20, 40, 80, 160],
        				pagination: true,
        				toolbar: "#message",
        				columns : [ [ {
        					field: 'rowNumber',
        					width: 25,
        					formatter: function(value, row, index){
        						return index + 1;
        					}
        				}, {
        					field: 'questionTitle',
        					title: '问题编号'
        				},  {
                             field: 'questionId',
                             title: '问题id'
                        },
        				{
        					field: 'feedbacker',
        					title: '提交人'
        				}, {
        					field: 'bugHeader',
        					title: 'BUG负责人'

        				}] ],
        				onLoadSuccess: function (data) {
        				console.log(data.total);
        				    if (data.total == 0) {
        				        var body = $(this).data().datagrid.dc.body2;
        				        body.find('table tbody').append('<tr><td colspan="9" width="' + body.width() + '" style="height: 35px; text-align: center;"><h1>暂无数据</h1></td></tr>');
        				    }
        				}
        			});*/

