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
            appendDeptData(data);
        },
        error: function(data){
            alert('error occurs while get listDepartment! ' + data);
        }
    });

    $('#confirmMdl').modal({backdrop: 'static', show: false});
    $('#addUserMdl').modal({backdrop: 'static', show: false});
    $(':radio[name="useroptradio"]').click(showUsersByRadio);
});

function appendDeptData(depts){
    var lastDept = $('#collapseDepartment tbody tr:last');
    var st = 1;
    if(lastDept.length != 0){
        st += parseInt(lastDept.find('td:first label').text());
    }
    $.each(depts, function(i, e){
        var html = '<tr><td><div class="form-check">' +
           '<label><input type="checkbox" class="form-check-input" value="">' + (i + st) + '</label></div></td>' +
           '<td>' + e.id + '</td>' +
           '<td>' + e.name + '</td>' +
           '<td>' + (e.manager == undefined ? '': e.manager) + '</td></tr>';
        $('#collapseDepartment tbody').append(html);
    });
}

function appendUnitData(deptName, units){
    var lastUnit = $('#collapseUnit tbody tr:last');
    var st = 1;
    if(lastUnit.length != 0){
        st += parseInt(lastUnit.find('td:first label').text());
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
        roles = roles.replace(/,$/, '');
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

function fetchCheckedDept(){
    var deptName = $('#collapseDepartment tbody tr :checked').first().closest('td').next().next().text();
    console.log("deptName:", deptName);
    if(deptName == undefined || deptName.length == 0){
        return;
    }
    $.ajax({
        url: 'getSection?' + $.param({sectionName: deptName}),
        dataType: 'json',
        success: function(data){
            console.log("return from fetchCheckedDept:", data);
            departmentData = data;
            //更新dept表的manager字段
            var manager = '';
            if(departmentData.manager != undefined){
                manager = departmentData.manager;
            }
            var curDept = $('#collapseDepartment tbody tr').filter(function(){
                return $(this).find('td:eq(1)').text() == departmentData.id;
            });
            curDept.find('td:eq(3)').text(manager);
            //更新其他表
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

function deleteAndConfirmDept(){
    if(departmentData == undefined){
        alert("请先获取需要删除的部门数据，再做删除操作！");
    }

    //获取要删除的部门，存成formData形式发送到后台
    var formData = new FormData();
    formData.append("deptId", departmentData.id);
    var html = "<p>是否要删除部门：" + departmentData.name + "？</p>";
    openConfirmModal(html, formData, function(fd){
        $.ajax({
            url: 'deleteDept',
            type: 'POST',
            data: fd,
            processData: false,
            contentType: false,
            success: function(data){
                //在部门表中删除对应行,清空处表，用户表
                departmentData = undefined;
                var deptId = fd.get("deptId");
                $('#collapseDepartment tbody tr').filter(function(){
                    return $(this).find('td:eq(1)').text() == deptId;
                }).remove();
                $('#collapseUnit tbody').empty();
                $('#collapseUser tbody').empty();
            },
            error: function(err){
                alert("删除失败! " + err.status + ":" + err.statusText);
            }
        });
    });
}

function submitDeptEdit(){
    var deptName = $('#deptDepartment').val();
    var manager = $('#deptManager').val();
    if(manager == '') return;

    $.ajax({
        url: 'addUsers',
        type: 'POST',
        data: JSON.stringify({name: deptName, manager: manager}),
        contentType: 'application/json; charset=UTF-8',
        success: function(response){
            console.log('submitDeptEdit success:', response);
            //更新全局变量deparmentData, 更新界面
            if(response.manager == undefined){
                departmentData.manager = '';
            }else{
                departmentData.manager = response.manager;
            }
            $('#collapseDepartment tbody tr').filter(function(){
                return $(this).find('td:eq(2)').text() == departmentData.name;
            }).find('td:eq(3)').text(departmentData.manager);
        },
        error: function(response){
            alert('submitDeptEdit error! ' + response.status + ":" + response.statusText);
        }
    });

    $('#addDeptMdl').modal('hide');
    return false;
}

function openEditDeptDlg(){
    if(departmentData == undefined){
        alert("请先获取部门数据，再编辑此部门！");
        return;
    }
    $('#deptDepartment').val(departmentData.name);
    $('#deptDepartment').prop('disabled', true);
    $('#deptManager').closest('div').show();
    $('#deptManager').val('');
    if(departmentData.manager != undefined){
        $('#deptManager').val(departmentData.manager);
    }
    $('#deptAddOrEdit').text('编辑');
    document.getElementById('deptSubmitBtn').onclick = submitDeptEdit;
    $('#addDeptMdl').modal('show');
}

function submitDeptAdd(){
    var deptName = $('#deptDepartment').val();
    if(deptName == '') return;
    $.ajax({
        url: 'addUsers',
        type: 'POST',
        data: JSON.stringify({name: deptName}),
        contentType: 'application/json;charset=UTF-8',
        DataType: 'json',
        success: function(data){
            //console.log("submitDeptForm success:", data);
            departmentData = data;
            appendDeptData([data,]);
            $('#collapseUnit tbody').empty();
            $('#collapseUser tbody').empty();
        },
        error: function(response){
            alert('submitDeptAdd error! ' + response.status + ":" + response.statusText);
        }
    });
    $('#addDeptMdl').modal('hide');
    return false;
}

function openAddDeptDlg(){
    $('#deptDepartment').val('');
    $('#deptDepartment').prop('disabled', false);
    $('#deptManager').closest('div').hide();
    $('#deptAndOrEdit').text('添加');
    document.getElementById('deptSubmitBtn').onclick = submitDeptAdd;
    $('#addDeptMdl').modal('show');
}

//先从后台删除处，然后在departmentData中删除，再在html dom中删除.
function deleteAndConfirmUnits(){
    var units = [];
    //获取要删除的处，存成formData形式发送到后台
    $('#collapseUnit tbody tr').filter(function(){
        return $(this).find('td:eq(0) :checked').length > 0;
    }).each(function(){
        var id = $(this).find('td:eq(1)').text();
        var secName = $(this).find('td:eq(2)').text();
        var unitName = $(this).find('td:eq(3)').text();
        units.push({id: id, name: secName + '---' + unitName});
    });
    var html = "<p>是否要删除如下的处及其下的用户数据：</p><p>" +
    units.map(function(cur){return cur.name}).join(",") + "</p>";
    openConfirmModal(html, units.map(function(cur){return cur.id}), function(ids){
        var formData = ids.reduce(function(total, item){
            total.append("unitId", item);
            return total;
        }, new FormData());

        $.ajax({
            url: 'deleteUnits',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: function(data){
                var delUnits = [];
                //更新departmentData中的处信息
                var hasNotUnitId = function(unit, unitIds){
                    for(var i=0; i<unitIds.length; i++){
                        if(unitIds[i] == unit.id) {
                            delUnits.push(unit);
                            return false;
                        }
                    }
                    return true;
                };
                departmentData.units = departmentData.units.filter(function(cur){
                    return hasNotUnitId(cur, data);
                });
                //更新界面内容, 删除掉unit及其下的user
                var hasUnitByName = function(unitName, units){
                    for(var i=0; i<units.length; i++){
                        if(units[i].name == unitName) return true;
                    }
                    return false;
                };
                $('#collapseUnit tbody tr').filter(function(){
                    //console.log('delete unit doc:', $(this).find('td:eq(3)').text(), delUnits);
                    return hasUnitByName($(this).find('td:eq(3)').text(), delUnits);
                }).remove();
                $('#collapseUser tbody tr').filter(function(){
                    return hasUnitByName($(this).find('td:eq(2)').text(), delUnits);
                }).remove();

            },
            error: function(err){
                alert("删除失败，返回值：" + err.status);
            }
        });
    });
}

function submitUnitForm() {
    var deptName = $('#unitDepartment').text();
    var unitName = $('#unitUnit').val();
    var manager = $('#unitManager').val();
    //unitName 不能已经存在
    var addUnits = departmentData.units.filter(function(cur){
        return cur.name == unitName;
    });
    if(addUnits.length > 0){
        alert("处名（" + unitName + "）已经存在不能再添加！");
        return;
    }
    if(manager != undefined && manager.length > 0){
        //TODO 校验manager必须已经存在，才允许提交
    }
    $.ajax({
        url: 'addUsers',
        type: 'POST',
        data: JSON.stringify({name: deptName,
            units: [{ name: unitName, manager: manager
            }]
        }),
        contentType: 'application/json;charset=UTF-8',
        DataType: 'json',
        success: function(response){
            var unit = response.units[0];
            //console.log("addUnit success:", unit);
            departmentData.units.push(unit);
            appendUnitData(departmentData.name, [unit,]);
        },
        error: function(response){
            alert('submitUnitForm error! ' + response.status + ":" + response.statusText);
        }
    });

    $('#addUnitMdl').modal('hide');
    return false;
}

function openAddUnitDlg(){
    if(departmentData == undefined || departmentData.name == undefined){
        alert('没有找到部门名称，请先选到某个部门！');
        return;
    }

    $('#unitDepartment').text(departmentData.name);
    $('#unitUnit').val('');
    $('#unitManager').val('');
    $('#addUnitMdl').modal('show');
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

//把section中的用户与全局变量departmentData中的用户合并，并刷新界面.
//待合并用户的处是存在的
function addUserAndRefresh(section){
    //console.log("success from submit user:", section);
    if(section.staffs != undefined && section.staffs.length > 0){
        departmentData.staffs = departmentData.staffs.concat(section.staffs);
    }
    if(section.units != undefined){
        $.each(section.units, function(idx, unit){
            units = departmentData.units.filter(function(u){
                return u.name == unit.name;
            });
            if(units.length == 0) return;
            units[0].staffs = units[0].staffs.concat(unit.staffs);
        });
    }

    showUsersByRadio();
}


function submitUserForm(){
    var fd = new FormData(document.getElementById('addUserForm'));
    fd.append('userId', fd.get('email'));
    fd.set('email', fd.get('email') + '@inspur.com');
    fd.set('number', 'LCJB' + fd.get('number'));
    fd.set("sectionName", $('#userDepartment').text());
    $.ajax({
        url: 'addUser',
        type: 'POST',
        data: fd,
        processData: false,
        contentType: false,
        DataType: 'json',
        success: function(response,status,xhr){
            addUserAndRefresh(response);
        },
        error: function(data){
            alert('submitUserForm error!' + data);
        }
    });

    $('#addUserMdl').modal('hide');
    return false;
}

function openAddUserDlg(){
    if(departmentData == undefined || departmentData.name == undefined){
        alert('没有找到部门名称，请先选到某个部门！');
        return;
    }

    $('#userDepartment').text(departmentData.name);
    $('#userUnit').empty();
    $('#userUnit').append('<option value="无">无</option>');
    $.each(departmentData.units, function(idx, e){
        $('#userUnit').append('<option value="' + e.name + '">' + e.name + '</option>');
    });
    $('#userName').val('');
    $('#userNumber').val('');
    $('#userEmail').val('');
    $('#userRoles').find(':selected').prop('selected', false);
    $('#addUserMdl').modal('show');
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

function toggleChecks(self){
    if(self.checked){
        $(self).closest('thead').next().find(':checkbox').prop('checked', true);
    }else{
        $(self).closest('thead').next().find(':checked').prop('checked', false);
    }
}
