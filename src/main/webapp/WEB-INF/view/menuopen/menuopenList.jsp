<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2016/1/20
  Time: 10:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>


    <script src="/static/jquery/jquery.min.js" type="text/javascript"></script>
    <script src="/static/jquery/jquery.form.js" type="text/javascript"></script>
    <link href="/static/jquery/themes/icon.css" rel="stylesheet" type="text/css"/>
    <link href="/static/jquery/themes/color.css" rel="stylesheet" type="text/css"/>
    <link href="/static/jquery/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
    <script src="/static/jquery/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="/static/jquery/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
    <script type="text/javascript">

        $(function () {
            initTable();
        });

        function initTable() {
            $("#menuopenTable").datagrid({
                url: "/menuopen/menuopenList2",
                method: "post",
                idField: "id",
                fitColumns: true,
                fit: true,
                striped: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                autoRowHeight: true,
                toolbar: toolBar,
                pageSize: 20,
                pageList: [10, 15, 20, 25, 30],
                    frozenColumns: [[
                        {
                            title: "id",
                            field: "id",
                            width: 50,
                            hidden: true
//                                checkbox: true
                        }]],
                    columns: [[
                        {
                            title: "是否开放",
                            field: "isOpen",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                }  else if (data == 0) {
                                    return "否";
                                } else if (data == 1) {
                                    return "是";
                                }
                            },
                            width: 20
                        },
                        {
                            title: "标识",
                            field: "code",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else {
                                    return data;
                                }
                            },
                            width: 20
                        }
                        ,
                        {
                            title: "作用域",
                            field: "forsource",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else if(data == 0){
                                    return "所有";
                                } else if(data == 1){
                                    return "iOS";
                                } else if(data == 2){
                                    return "Android";
                                }
                            },
                            width: 8
                        }
                    ]]
                }
            );
        }

        /**
         *open edit div
         */
        function openAddDiv() {
            var data = $("#menuopenTable").datagrid("getSelected");
            if (data == null) {
                $.messager.confirm('提示', '请选中一条记录进行操作!');
                return;
            }

            $("#edit_id").val(data.id);

            $('#edit_isOpen').combobox('select',data.isOpen);


            $('#editDiv').window('open');  // open a window
            $('#editDiv').window('center');
        }

        /**
         * close edit div
         */
        function cancelEditDiv() {
            $('#editDiv').window('close');
        }

        /**
         * commit edit form
         */
        function editMenuopen() {

            //表单验证
            if (!$("#editForm").form('validate')) {
                return false;
            }

            $('#editDiv').window('close');

            $.messager.confirm('提示', '确定修改信息？', function (r) {
                if (r) {

                    $("#editForm").ajaxSubmit(function (resultJson) {
                        //回调操作
                        $.messager.confirm("提示", resultJson.msg);
                        $("#menuopenTable").datagrid("reload");
                        $('#editDiv').window('close');
                    });

                }
            });
        }

    </script>
</head>
<body>
<div id="toolBar">


    <a class="easyui-linkbutton" onclick="openAddDiv(false)" data-options="iconCls:'icon-edit'"
       style="margin:5px 5px 5px 0px;">修改</a>

</div>
<table id="menuopenTable" style="width:auto;"></table>


<div id="editDiv" class="easyui-window" title="修改信息" style="width:300px;height:300px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" style="height:80%;">
            <form id="editForm" action="/menuopen/updateMenuopen" method="post">
                <input id="edit_id" name="id" type="hidden"/>
                <table style="text-align: center;width: 100%;height:90%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td>是否开放:</td>
                        <td><input class="easyui-combobox" id="edit_isOpen" name="isOpen"
                                   data-options="
                                                valueField: 'value',
                                                textField: 'label',
                                                data: [{
                                                    label: '开放',
                                                    value: '1'
                                                },{
                                                    label: '不开放',
                                                    value: '0'
                                                }]"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'" style="text-align:center;height:20%">
            <a class="easyui-linkbutton" onclick="editMenuopen()" data-options="iconCls:'icon-ok'"
               style="margin: 5px">确定</a>
            <a class="easyui-linkbutton" onclick="cancelEditDiv()" data-options="iconCls:'icon-cancel'"
               style="margin: 5px">取消</a>
        </div>
    </div>
</div>


</body>
</html>
