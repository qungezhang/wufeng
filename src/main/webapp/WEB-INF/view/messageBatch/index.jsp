<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2016/3/3
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>消息模板</title>

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
            $("#messageBatchTable").datagrid({
                        url: "/messageBatch/list",
                        method: "post",
                        idField: "msgid",
                        remoteSort: true,
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
                                title: "编号",
                                field: "mbid",
                                hidden: true
                            }]],
                        columns: [[
                            {
                                title: "mbid",
                                field: "mbid",
                                width: 10,
                                hidden: true
                            }, {
                                title: "标题",
                                field: "title",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 10
                            }, {
                                title: "内容",
                                field: "message",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 10
                            }, {
                                title: "发送人",
                                field: "mtype",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else if (data == 1) {
                                        return "系统";
                                    } else if (data == 2) {
                                        return "人工";
                                    }
                                },
                                width: 10
                            },{
                                title: "接收人",
                                field: "rtype",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else if (data == 1) {
                                        return "个人";
                                    } else if (data == 2) {
                                        return "所有人";
                                    }
                                },
                                width: 10
                            }, {
                                title: "手机号",
                                field: "tel",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    }else {
                                        return data;
                                    }
                                }
                                ,
                                width: 10
                            }, {
                                title: "状态",
                                field: "sended",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else if (data == 0) {
                                        return "未推送";
                                    } else {
                                        return "已推送";
                                    }
                                }
                                ,
                                width: 10
                            },
                            {
                                title: "操作",
                                field: "operate",
                                formatter: function (data, rowData) {
                                    if (data == null) {
                                        return "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='推送' type='button' onclick='sendMessage(" + JSON.stringify(rowData) + ")'/>";
                                    } else {
                                        return "";
                                    }
                                },
                                width: 10
                            }
                        ]]
                    }
            )
            ;
        }

        var isSubmitAdd;
        function openAddDiv(isAdd) {
            isSubmitAdd = isAdd;
            if (isAdd) {

                $('#addDiv').window({title: "增加"});
                $('#addDiv').window('open');  // open a window
                $('#addDiv').window('center');


                var inputs = $("#addForm input.easyui-textbox");
                inputs.textbox("setText", "");
                inputs.textbox("setValue", "");
                $('#add_suid').val("");
            } else {
                var data = $("#messageBatchTable").datagrid("getSelected");
                if (data == null) {
                    $.messager.confirm('提示', '请选中一条记录进行操作!');
                    return;
                }
                $('#addDiv').window({title: "修改"});
                $('#addDiv').window('open');  // open a window
                $('#addDiv').window('center');


                $('#add_suid').val(data.mbid);


                $('#add_title').textbox('setText',data.title);
                $('#add_title').textbox('setValue',data.title);

                // $('#add_tel').textbox('setText',data.tel);
                // $('#add_tel').textbox('setValue',data.tel);

                $('#add_message').val(data.message);

                // $('#add_mtype').combobox('select',data.mtype);
                //
                // $('#add_rtype').combobox('select',data.rtype);

            }
        }

        function addMessageBatch() {

            // var rtype = $('#add_rtype').combobox("getValue");
            // if (rtype == 1) {
            //     var mobile = $('#add_tel').textbox('getValue');
            //     if (mobile == '') {
            //         alert("请输入手机号码");
            //         return;
            //     }
            // }

            $('#addDiv').window('close');

            if (isSubmitAdd) {
                $("#addForm").attr("action", "/messageBatch/add");
                $("#addForm").ajaxSubmit(function (resultJson) {
                    //回调操作
                    $.messager.confirm("提示", resultJson.msg);
                    $("#messageBatchTable").datagrid("reload");
                });
            } else {
                $("#addForm").attr("action", "/messageBatch/add");
                $("#addForm").ajaxSubmit(function (resultJson) {
                    //回调操作
                    $.messager.confirm("提示", resultJson.msg);
                    $("#messageBatchTable").datagrid("reload");
                });

            }
        }

        function cancelAddDiv() {
            $('#addDiv').window('close');
        }

        function sendMessage(data) {
            var mbid = data.mbid;
            $.ajax({
                url: "/messageBatch/sendJiGMessage",
                dataType: "json",
                type: "post",
                data: {
                    mbid: mbid
                },
                error: function () {
                    $.messager.confirm("提示","系统错误");
                },
                success: function (data) {
                    $.messager.confirm("提示", "推送成功");
                    $("#messageBatchTable").datagrid("reload");
                }
            });
        }

        function removeMessageBatch() {
            var data = $("#messageBatchTable").datagrid("getSelected");
            if (data == null) {
                $.messager.confirm('提示', '请选中一条记录进行操作!');
                return;
            }
            var mbid = data.mbid;
            $.messager.confirm('提示', '确定要删除这条记录吗？', function (r) {
                if (r) {
                    $.ajax({
                        url: "/messageBatch/remove",
                        dataType: "json",
                        type: "post",
                        data: {
                            mbid: mbid
                        },
                        error: function () {
                            alert("系统错误!");
                        },
                        success: function (data) {
                            $.messager.confirm("提示", data.msg);
                            $("#messageBatchTable").datagrid("reload");
                        }
                    });
                }
            });
        }


    </script>


</head>
<body>
<div id="toolBar">

    <a class="easyui-linkbutton" onclick="openAddDiv(true)" data-options="iconCls:'icon-add'"
       style="margin:5px 5px 5px 0px;">新增</a>
    <a class="easyui-linkbutton" onclick="openAddDiv(false)" data-options="iconCls:'icon-edit'"
       style="margin:5px 5px 5px 0px;">修改</a>
    <a class="easyui-linkbutton" onclick="removeMessageBatch()" data-options="iconCls:'icon-remove'"
       style="margin:5px 5px 5px 0px;">删除</a>

</div>
<table id="messageBatchTable"></table>

<div id="addDiv" class="easyui-window" title="添加" style="height:300px;width:600px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" style="height:80%;">
            <form id="addForm" method="POST">
                <input id="add_suid" name="mbid" type="hidden"/>
                <table style="text-align: center;width: 100%;height:90%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td>标题:</td>
                        <td><input class="easyui-textbox" id="add_title" name="title" type="text" style="width: 80%;"/></td>
                    </tr>
                    <tr>
                        <td>消息内容:</td>
                        <td><textarea id="add_message" name="message" type="text" style="width: 80%;height: 100px;resize:none;border: 1px solid #95B8E7;border-radius: 5px 5px 5px 5px;" ></textarea></td>
                        <%--<td><input class="easyui-textbox" id="add_message" name="message" type="text"/></td>--%>
                    </tr>
                    <%--<tr>--%>
                        <%--<td>发送人:</td>--%>
                        <%--<td><input class="easyui-combobox" id="add_mtype" name="mtype"--%>
                                   <%--data-options="--%>
                                                <%--valueField: 'value',--%>
                                                <%--textField: 'label',--%>
                                                <%--data: [{--%>
                                                    <%--label: '系统',--%>
                                                    <%--value: '1'--%>
                                                <%--},{--%>
                                                    <%--label: '人工',--%>
                                                    <%--value: '2'--%>
                                                <%--}]"/>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                        <%--<td>接受人:</td>--%>
                        <%--<td><input class="easyui-combobox" id="add_rtype" name="rtype"--%>
                                   <%--data-options="--%>
                                                <%--valueField: 'value',--%>
                                                <%--textField: 'label',--%>
                                                <%--data: [{--%>
                                                    <%--label: '个人',--%>
                                                    <%--value: '1'--%>
                                                <%--},{--%>
                                                    <%--label: '所有人',--%>
                                                    <%--value: '2'--%>
                                                <%--}]"/>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                        <%--<td>手机号:<br/>(接收人为个人时，请输入接收人手机号码)</td>--%>
                        <%--<td><input class="easyui-textbox" id="add_tel" name="tel" type="text"/></td>--%>
                    <%--</tr>--%>
                </table>
            </form>
        </div>
        <div data-options="region:'center'" style="text-align:center;">
            <a class="easyui-linkbutton" onclick="addMessageBatch()" data-options="iconCls:'icon-ok'"
               style="margin: 5px">确定</a>
            <a class="easyui-linkbutton" onclick="cancelAddDiv()" data-options="iconCls:'icon-cancel'"
               style="margin: 5px">取消</a>
        </div>
    </div>
</div>


</body>
</html>
