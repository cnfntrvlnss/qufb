$(document).ready(function(){
    $('#collapseDepartment tbody').empty();
    $('#collapseUnit tbody').empty();
    $('#collapseUser tbody').empty();
    $.ajax({
        url: "listDepartment",
        dataType:"json",
        success: function(data){
            console.log('data:', data);
            $('#collapseDepartment tbody').empty();
            $.each(data, function(i, e){
                var html = '<tr><td><div class="form-check">' +
                   '<label><input type="checkbox" class="form-check-input" value="">' + (i + 1) + '</label></div></td>' +
                   '<td>' + e.id + '</td>' +
                   '<td>' + e.name + '</td>' +
                   '<td>' + (e.manager == undefined ? '': e.manager) + '</td></tr>';
                $('#collapseDepartment tbody').append(html);
            });
        },
        error: function(data){
            alert('error occurs while get listDepartment! ' + data);
        }
    });

    $('#confirmMdl').modal({backdrop: 'static', show: false});

    $(':radio[name="useroptradio"]').click(showUsersByRadio);
});

function appendUnitData(deptName, units){
    var lastUnit = $('#collapseUnit tbody tr:last');
    var st = 1;
    if(lastUnit.length != 0){
        st += parseInit(lastUnit.find('td:first label').text());
    }
    $.each(units, function(i, e){
        var html = '<tr><td><div class="form-check"><label>' +
                   '<input type="checkbox" class="form-check-input" value="">'+ (i + st) + '</label></div></td>' +
                    '<td>' + e.id + '</td>' +
                    '<td>' + deptName + '</td>' +
                    '<td>' + e.name + '</td>' +
                    '<td>' + (e.manager == undefined ? '': e.manager) + '</td></tr>';
        $('#collapseUnit tbody').append(html);

    });
}

function appendUserData(deptName, unitName, staffs){
    var lastUser = $('#collapseUser tbody tr:last');
    var st = 1;
    if(lastUser.length != 0){
        var lastIdx = lastUser.find('td:first label').text();
        st += parseInt(lastIdx);
    }
     console.debug('append to user table, st: ', st);
    $.each(staffs, function(i, e){
        var roles = e.roles.reduce(function(total, value){
            return total  + value + ',';
        }, '');
        roles = roles.replace(',$', '');
        var html = '<tr><td><div class="form-check"><label>' +
                   '<input type="checkbox" class="form-check-input" value="">'+ (i+st) + '</label></div></td>' +
                   '<td>'+ deptName + '</td>' +
                   '<td>'+ unitName +'</td>' +
                   '<td>' + e.userId + '</td>' +
                   '<td>' + e.name + '</td>' +
                    '<td>' + e.number + '</td>' +
                    '<td>' + e.email + '</td>' +
                    '<td>' + roles + '</td></tr>';
        $('#collapseUser tbody').append(html);
    });
}

var departmentData;
/*
var deletedUserIds = [];
var addedUsers = [];

function commitAll(){
    var html = '';
    if(deletedUserIds.length > 0){
        html += '<p>删除的用户：' + JSON.stringify(deletedUserIds) + '</p>';
    }
    if(addedUsers.length > 0){
        html += '<p>添加的用户：' + JSON.stringify(addedUsers) + '</p>';
    }

    if(html.length == 0){
        return;
    }
    openConfirmModal(html, {}, function(){
        if(departmentData.length > 0){
            $.ajax({
                url: 'deleteUsers'
            });
        }
    });
}

function revertAll(){
    deletedUserIds = [];
    addedUsers = [];
}
*/

function fetchCheckedDept(){
    var deptName = $('#collapseDepartment tbody tr :checked').closest('td').next().next().text();
    console.log("deptName:", deptName);
    if(deptName == undefined || deptName.length == 0){
        return;
    }
    $.ajax({
        url: 'getSection?' + $.param({sectionName: deptName}),
        dataType: 'json',
        success: function(data){
            console.log("return from fetchCheckedDept:", data);
            departmentData = data.departments[0];
            $('#collapseUnit tbody').empty();
            appendUnitData(departmentData.name, departmentData.units);
            showUsersByRadio();
        },
        error: function(data){
        }
    });
}

function showUsersByRadio(){
        var val = $(':radio[name="useroptradio"]:checked').val();
        if(val == 'all'){
            showAllUsers();
        }else{
            showCheckedUsers();
        }
}

function showAllUsers(){
    $('#collapseUser tbody').empty();
    appendUserData(departmentData.name, '无', departmentData.staffs);
    var units = departmentData.units;
    for(var i=0; i< units.length; i++){
        appendUserData(departmentData.name, units[i].name, units[i].staffs);
    }
}

function showCheckedUsers(){
    $('#collapseUser tbody').empty();
    if(departmentData == undefined || departmentData.id == undefined) return ;

    //department被checked了，才显示相应的users
    var deptChk = $('#collapseDepartment tbody tr').filter(function(idx){
        var deptId = $(this).children().eq(1).text();
        if( departmentData.id == parseInt(deptId)) return true;
        else return false;
    }).find('td:first :checkbox');
    if(deptChk.prop('checked') == true){
        appendUserData(departmentData.name, '无', departmentData.staffs);
    }

    //显示checked了的unit下的用户
    $('#collapseUnit tbody tr :checked').each(function(){
        var unitId = parseInt($(this).closest('td').next().text());
        $.each(departmentData.units, function(idx, u){
            if(u.id == unitId){
                appendUserData(departmentData.name, u.name, u.staffs);
            }
        });
    });
}

//从departmentData中删除userId为userIds的用户, 并刷新用户表
function removeUsersAndRefresh(userIds){
    var usermap = userIds.reduce(function(m, u){
        m[u] = null;
        return m;
    }, {});
    if(Object.keys(usermap).length > 0){
        departmentData.staffs = departmentData.staffs.filter(function(user){
            if(user.userId in usermap){
                delete usermap[user.userId];
                return false;
            }
            return true;
        });
    }
    $.each(departmentData.units, function(idx, unit){
        if(Object.keys(usermap).length == 0) return;
        unit.staffs = unit.staffs.filter(function(user){
            if(user.userId in usermap){
                delete usermap[user.userId];
                return false;
            }
            return true;
        });
    });

    showUsersByRadio();
}

function deleteUsers(){
    var userIds = [];
    $('#collapseUser tbody tr').filter(function(){
             return $(this).find('td:eq(0) :checked').length > 0;
         }).find('td:eq(3)').each(function(){userIds.push($(this).text());});
    openConfirmModal('<p>是否要删除这些用户？</p><p>' + JSON.stringify(userIds) + '</p>',
        userIds,
        function(ids){
          $.ajax({
              url: 'deleteUsers',
              type: 'POST',
              contentType : 'application/json;charset=UTF-8',
              data: JSON.stringify(ids),
              success: function(data){
                  removeUsersAndRefresh(ids);
              },
              error: function(data){
                  alert('提交出错了！');
              }
          });
    });
}

function toggleChecks(self){
    if(self.checked){
        $(self).closest('thead').next().find(':checkbox').prop('checked', true);
    }else{
        $(self).closest('thead').next().find(':checked').prop('checked', false);
    }
}

function openConfirmModal(body, args, fn) {
    var mdl = $('#confirmMdl');
    mdl.find('.modal-body').empty();
    mdl.find('.modal-body').append(body);
    mdl.find('button.confirmed').click({args: args, fn: fn}, function(event) {
        event.data.fn(event.data.args);
        $(this).unbind(event);
        mdl.modal('hide');
    });
    mdl.modal('show');
}