        /*$.ajax({
            url: "/listQuestion",
            success: function(data){
                console.log("questions:", data);
                $.each(data, function(i, e){
                     $('#questions').append('<li>' + e.questionId+ ' --- ' + e.questionTitle + '</li>');
                 });
            },
            error: function(data){
                alert("error!!! " + data);
            }
        });*/
        $.ajax({
            url: "/listQuestion",
            success: function(data){
                console.log("questions:", data);
                $.each(data, function(i, e){
                     $('#questionTable').append('<tr><td>' + e.questionId + '</td><td>' + e.questionTitle+ '</td><td>' + e.bugHeader + '</td><td>' + e.questionDescription + '</td> <td>' + e.feedbackSuggestion+ '</td> <td>' + e.feedbackTime+'</td></tr>');
                 });
            },
            error: function(data){
                alert("error!!! " + data);
            }
        });

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

