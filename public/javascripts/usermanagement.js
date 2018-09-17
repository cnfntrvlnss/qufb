$(document).ready(function(){
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
     console.log('st: ', st);
    $.each(staffs, function(i, e){
        var html = '';
    });
}

var departmentData;
function fetchCheckedDept(){
    var deptName = $('#collapseDepartment tbody tr :checkbox').first().closest('td').next().next().text();
    //console.log("deptId:", deptId);
    $.ajax({
        url: 'getSection?' + $.param({sectionName: deptName}),
        dataType: 'json',
        success: function(data){
            console.log("return from fetchCheckedDept:", data);
            departmentData = data.departments[0];
            $('#collapseUnit tbody').empty();
            appendUnitData(departmentData.name, departmentData.units);
            appendUserData(departmentData.name, '无', departmentData.staffs);

        },
        error: function(data){
        }
    });
}