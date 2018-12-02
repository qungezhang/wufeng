<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2016/4/13
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
            $("#withdrawTable").datagrid({
                        url: "/withdraw/listWithdraw",
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
                        pageSize: 10,
                        pageList: [10, 15, 20, 25, 30],
                        frozenColumns: [[
                            {
                                title: "编号",
                                field: "id",
                                hidden: true
//                                checkbox: true
                            }]],
                        columns: [[
                            {
                            title: "提现编号",
                            field: "wid",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else {
                                    return data;
                                }
                            },
                            width: 8
                            },
                            {
                                title: "提现用户",
                                field: "username",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 8
                            }, {
                                title: "联系方式",
                                field: "mobile",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 8
                            }, {
                                title: "用户积分",
                                field: "point",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 6
                            }, {
                                title: "提现金额",
                                field: "amount",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return "￥" + data;
                                    }
                                },
                                width: 6
                            },{
                                title: "提现方式",
                                field: "type",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else if (data == 1) {
                                        return "支付宝";
                                    } else if (data == 2)  {
                                        return "微信";
                                    }
                                },
                                width: 6
                            }, {
                                title: "提现账户",
                                field: "account",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 10
                            }, {
                                title: "申请时间",
                                field: "createTime",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return DateTimeFormatter(data);
                                    }
                                },
                                width: 12
                            },{
                                title: "状态",
                                field: "status",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else if (data == 1) {
                                        return "已打款";
                                    } else if (data == -1)  {
                                        return "打款失败";
                                    } else {
                                        return "申请中";
                                    }
                                },
                                width: 4
                            },{
                                title: "备注",
                                field: "remark",
                                formatter: function (data, rowData) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        if(0 != rowData.status){
                                            return data;
                                        }
                                        return "-";
                                    }
                                },
                                width: 10
                            },
                            {
                                title: "审核时间",
                                field: "operate",
                                formatter: function (data, rowData) {
                                    if (data == null) {
                                        var result = DateTimeFormatter(rowData.transferTime);
                                        if(0 == rowData.status){
                                            result = "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='审核' type='button' onclick='openExamineDiv(" + JSON.stringify(rowData) + ")'/>";
                                        }
                                        return result;
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


        function searchWithdraw() {
            var wid = $("#search_wid").textbox("getValue");
            var account = $("#search_account").textbox("getValue");
            var type = $("#search_type").combobox("getValue");
            var status = $("#search_status").combobox("getValue");
            $("#withdrawTable").datagrid({
                url: "/withdraw/listWithdraw",
                queryParams: {
                    wid: wid,
                    account:account,
                    type:type,
                    status:status
                }
            });
            $('#win').window('close');
        }

        function DateTimeFormatter(value) {
            var t, y, m, d, h, i, s;
            t = value ? new Date(value) : new Date();
            y = t.getFullYear();
            m = t.getMonth() + 1;
            d = t.getDate();
            h = t.getHours();
            i = t.getMinutes();
            s = t.getSeconds();
            return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i) + ':' + (s < 10 ? '0' + s : s);
        }

        function openExamineDiv(data) {

            $('#add_status').combobox('setValue','1');
            $("#add_remark").val("");
            $("#wid").val(data.wid);

            $('#examineDiv').window({title: "审核"});
            $('#examineDiv').window('open');  // open a window
            $('#examineDiv').window('center');

        }

        function cancelExamineDiv() {
            $('#examineDiv').window('close');
        }

        function addExamine(){

            $("#examineForm").attr("action", "/withdraw/examineWithdraw");
            $("#examineForm").ajaxSubmit(function (resultJson) {
                //回调操作
                $.messager.confirm("提示", resultJson.msg);
                $("#withdrawTable").datagrid("reload");
                $('#examineDiv').window('close');
            });
        }

    </script>
</head>
<body>
<div id="toolBar">
    <br/>
    提现编号：<input class="easyui-textbox" id="search_wid" type="text"/>
    提现账号：<input class="easyui-textbox" id="search_account" type="text"/>
    提现方式：
    <select id="search_type" class="easyui-combobox" name="type" style="width: 145px;">
        <option value="">全部</option>
        <option value="1">支付宝</option>
        <option value="2">微信</option>
    </select>
    状态：
    <select id="search_status" class="easyui-combobox" name="status" style="width: 145px;">
        <option value="">全部</option>
        <option value="0">申请中</option>
        <option value="1">已打款</option>
        <option value="-1">打款失败</option>
    </select>
    <a class="easyui-linkbutton" onclick="searchWithdraw()" data-options="iconCls:'icon-search'"
       style="margin:5px 5px 5px 0px;">查询</a>
    <br/>
</div>
<table id="withdrawTable" style="width:auto;"></table>

<div id="examineDiv" class="easyui-window" title="转账操作" style="height:300px;width:400px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" style="height:80%;">
            <form id="examineForm" method="POST">
                <table style="text-align: center;width: 100%;height:90%" border="0" cellpadding="2" cellspacing="0">
                    <input id="wid" name="wid" hidden="hidden"/>
                    <tr>
                        <td>操作:</td>
                        <td><select class="easyui-combobox" id="add_status" name="status" style="width:80%">
                            <option value="1" selected="selected">已转账</option>
                            <option value="-1">转账失败</option>
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td>备注:<br/>(转账失败原因)</td>
                        <td>
                            <textarea id="add_remark" name="remark" type="text" style="width: 80%;height: 100px;resize:none" ></textarea>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'" style="text-align:center;">
            <a class="easyui-linkbutton" onclick="addExamine()" data-options="iconCls:'icon-ok'"
               style="margin: 5px">确定</a>
            <a class="easyui-linkbutton" onclick="cancelExamineDiv()" data-options="iconCls:'icon-cancel'"
               style="margin: 5px">取消</a>
        </div>
    </div>
</div>
</body>
</html>
