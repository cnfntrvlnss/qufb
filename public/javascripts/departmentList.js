
        var classCss;
        $.ajax({
            url: "/getFirstDepartment",
            success: function(data){
                $.each(data, function(i, e){
                    if(i % 2==0){
                        classCss="even";
                    }else{
                        classCss="odd";
                    }
                     $('#departmentTable').append('<tr class='+classCss+'><td>' + '<a href="#" onclick="getSecondDepartment('+e.departmentId+')">'+e.departmentId+'</a>'+ '</td><td>' + '<a href="#" onclick="getSecondDepartment('+e.departmentId+')">'+e.departmentName+'</a>'+ '</td></tr>');
                 });
            },
            error: function(data){
                alert("error!!! " + data);
            }
        });
        function getSecondDepartment(departmentId){
            $('#secondDepartmentTable').empty();
             $.ajax({
                        url: "/getSecondDepartment/"+departmentId,
                        success: function(data){
                            $.each(data, function(i, e){
                                if(i % 2==0){
                                    classCss="even";
                                }else{
                                    classCss="odd";
                                }
                                 $('#secondDepartmentTable').append('<tr class='+classCss+'><td>' +e.departmentId+'</a>'+ '</td><td>' + '<a href="#" onclick="getUserList('+e.departmentId+')">'+e.departmentName+'</a>'+ '</td></tr>');
                             });
                        },
                        error: function(data){
                            alert("error!!! " + data);
                        }
                    });
        }
        function getUserList(departmentId){
             $('#userTable').empty();
                 $.ajax({
                            url: "/getUserList/"+departmentId,
                            success: function(data){
                                $.each(data, function(i, e){
                                    if(i % 2==0){
                                        classCss="even";
                                    }else{
                                        classCss="odd";
                                    }
                                     $('#userTable').append('<tr class='+classCss+'><td>' +e.userId+'</a>'+ '</td><td>' +e.userName+'</a>'+ '</td></tr>');
                                 });
                            },
                            error: function(data){
                                alert("error!!! " + data);
                            }
                        });

        }