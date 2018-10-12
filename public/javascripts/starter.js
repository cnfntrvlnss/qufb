$(function(){
    $('#qustAttrTable').bootstrapTable({
        toolbar: '#qustAttrTableTb',
        method: 'get',
        url: 'questionAttribute/fetchAll',
        search: true,
        //strictSearch: 'true',
        //singleSelect: true,
        clickToSelect: true,
        striped: true,
        idField: 'rowNum',
        checkboxHeader: true,
         columns: [{
                checkbox: true
             },
            {
                field: 'rowNum',
                title: '序号',
            }, {
                field: 'itemId',
                title: '配置ID'
            }, {
                field: 'itemName',
                title: '配置名称'
            }, {
                field: 'subItemId',
                title: '子配置ID'
            }, {
                field: 'subItemName',
                title: '子配置名称'
            }],

         responseHandler: function(data){
            return data.map(function(val, idx){
                var ret = {}
                ret.rowNum = idx + 1;
                ret.itemId = val.configId.configId;
                ret.itemName = val.configName;
                ret.subItemId = val.configId.subId;
                ret.subItemName = val.subName;
                return ret;
            })
         }
    })


})

$.validator.addMethod('equalExistItemName', function(value, element, param){
    var itemId = $(element).closest('form').find('input[name="itemId"]').val();
    if(itemId.length == 0) return true;
    var data = $('#qustAttrTable').bootstrapTable('getData');
    var row;
    data.some(function(item){
        if(item.itemId == itemId){
            row = item;
            return true;
        }
        return false;
    })
    if(row != undefined && row.itemName != value){
        return false;
    }
    return true;
})
$.validator.addMethod("existSubItem", function(value, element, param){
    var itemId = $(element).closest('form').find('input[name="itemId"]').val();
    if(itemId.length == 0) return true;
    var data = $('#qustAttrTable').bootstrapTable('getData');
    var row;
    data.some(function(item){
        if(item.itemId == itemId && item.subItemId == value){
            row = item;
            return true;
        }
        return false;
    })
    if(row != undefined){
        return false;
    }
    return true;
})
function validateAddItem(){
    $('#qustAttrDlg form').validate({
            rules: {
                itemId: {
                    required: true,
                    digits: true
                },
                itemName: {
                    required: true,
                    equalExistItemName: true
                },
                subItemId: {
                    required: true,
                    digits: true,
                    existSubItem: true
                },
                subItemName: "required"
            },
            messages:{
                itemId: {
                    required: "必需字段",
                    digits: "必须输入数字"
                },
                itemName: {
                    required: "必须字段",
                    equalExistItemName: "与已经存在的不相等"
                },
                subItemId: {
                    required: "必须字段",
                    digits: "必须输入数字",
                    existSubItem: "已经存在这个配置项"
                },
                subItemName: "必须字段"
            },
            errorElement: "em",
            errorPlacement: function(error, element){
                error.addClass("help-block");
                error.insertAfter(element);
            },
            highlight: function ( element, errorClass, validClass ) {
            	$( element ).parents( ".col-md-8" ).addClass( "has-error" ).removeClass( "has-success" );
            },
            unhighlight: function (element, errorClass, validClass) {
            	$( element ).parents( ".col-md-8" ).addClass( "has-success" ).removeClass( "has-error" );
            }
        })
}

function addQustAttr(mark){
    //重新创建配置信息对话框
    $('#qustAttrDlg').remove();

    var dlg = $('#qustAttrDlgHidden');
    var target = $('<div class="box" id="qustAttrDlg">').append(dlg.html());
    target.find('.box-title span').text(mark);
    dlg.before(target);
    validateAddItem();

    //这样添加的box remove动作无效，只能手动删除
    $('#qustAttrDlg [data-widget="remove"]').click(function(){
        $('#qustAttrDlg').slideUp(function(){
                $('#qustAttrDlg').remove();
        });
    })
    $('#qustAttrDlg button[type="submit"]').click(function(){
        if(!$('#qustAttrDlg form').valid()){
            return;
        }
        addOrUpdateAttr();
    })
}

function updateQustAttr(){
    var rows = $('#qustAttrTable').bootstrapTable('getSelections');
    if(rows.length == 0) return;
    //创建对话框，若存在就先删除.
    addQustAttr('编辑');
    $('#qustAttrDlgHidden').html();
    var row = rows[0];
    $('#qustAttrDlg [name=itemId]').val(row.itemId).prop('disabled', true);
    $('#qustAttrDlg [name=itemName]').val(row.itemName).prop('disabled', true);
    $('#qustAttrDlg [name=subItemId]').val(row.subItemId).prop('disabled', true);
    $('#qustAttrDlg [name=subItemName]').val(row.subItemName);
}

function addOrUpdateAttr(){
    var dlgMark = $('#qustAttrDlg .box-title span').text()
    if(dlgMark == '添加'){

    }
    var formData = new FormData();
    formData.append("itemId", $('#qustAttrDlg [name=itemId]').val());
    formData.append("itemName", $('#qustAttrDlg [name=itemName]').val());
    formData.append("subItemId", $('#qustAttrDlg [name=subItemId]').val());
    formData.append("subItemName", $('#qustAttrDlg [name=subItemName]').val());

    $('#qustAttrDlg').append('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');
    $.ajax({
        url: 'questionAttribute/addOrUpdate',
        method: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function(data){
            $('#qustAttrDlg> div:last').remove();
            $('#qustAttrTable').bootstrapTable('refresh');
        },
        error: function(xhr){
            $('#qustAttrDlg> div:last').remove();
            alert("提交失败！" + xhr.status + ":" + xhr.statusText);
        }
    })
}

function deleteQustAttrs() {
    var rows = $('#qustAttrTable').bootstrapTable('getSelections');
    if(rows.length == 0) return;
    openConfirmModal("<p>是否要删除这些配置项？共" + rows.length + "项。<p>", function(){
        var data = rows.map(function(val){
            return {
                "configId": val.itemId,
                "subId": val.subItemId
            }
        })
        $.ajax({
            url: 'questionAttribute/deleteSome',
            method: 'POST',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(data),
            success: function(res){
                $('#qustAttrTable').bootstrapTable('refresh');
            },
            error: function(xhr){
                alert("提交失败！" + xhr.status + ":" + xhr.statusText)
            }
        })
    });
}

function openConfirmModal(body, fn, args) {
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