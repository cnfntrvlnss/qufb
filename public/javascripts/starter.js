$(function(){
    $('#qustAttrTable').bootstrapTable({
        toolbar: '#qustAttrTableTb',
        method: 'get',
        url: 'assets/test.json',
        search: true,
        //strictSearch: 'true',
        singleSelect: true,
        striped: true,
        idField: 'rowNum',
         columns: [{
                field: 'rowNum',
                title: '序号',
                radio: true
            }, {
                field: 'itemId',
                title: '配置ID'
            }, {
                field: 'itemName',
                title: '配置名称'
            }, {
                field: 'subitemId',
                title: '子配置ID'
            }, {
                field: 'subitemName',
                title: '子配置名称'
            }],
         responseHandler: function(data){
         }

    })
})