
<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2016/2/14
  Time: 11:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>订单列表</title>

    <script src="/static/jquery/jquery.min.js" type="text/javascript"></script>
    <script src="/static/jquery/jquery.form.js" type="text/javascript"></script>
    <link href="/static/jquery/themes/icon.css" rel="stylesheet" type="text/css"/>
    <link href="/static/jquery/themes/color.css" rel="stylesheet" type="text/css"/>
    <link href="/static/jquery/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
    <script src="/static/jquery/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="/static/jquery/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>

    <script language=javascript>
        function clearNoNum(obj) {
            //先把非数字的都替换掉，除了数字和.
            obj.value = obj.value.replace(/[^\d. -]/g, "");
            //必须保证第一个为数字而不是.
            obj.value = obj.value.replace(/^\./g, "");
            //保证只有出现一个.而没有多个.
            obj.value = obj.value.replace(/\.{2,}/g, ".");
            //保证.只出现一次，而不能出现两次以上
            obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
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


    </script>

    <script type="text/javascript">
        $(function () {
            initTable();
        });

        function initTable() {
            $("#job").datagrid({
                        url: "/job/listJob",
                        method: "post",
                        idField: "jid",
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
                                field: "jid",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 100
                            }]],
                        columns: [[
                            {
                                title: "任务名称",
                                field: "name",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 20
                            },{
                                title: "雇主",
                                field: "employerName",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 10
                            },{
                                title: "联系方式",
                                field: "employerMobile",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 12
                            },
                            {
                                title: "价格",
                                field: "price",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return "¥"+data;
                                    }
                                },
                                width: 6
                            },
                            {
                                title: "订单状态",
                                field: "status",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else if (data == 1) {
                                        return "待审核";
                                    } else if (data == 2)  {
                                        return "审核通过";
                                    } else if (data == 4)  {
                                        return "任务下架";
                                    } else if (data == 5)  {
                                        return "任务完成";
                                    }else {
                                        return "待领取";
                                    }
                                },
                                width: 8

                            },{
                                title: "领取人",
                                field: "receiveName",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 10
                            },{
                                title: "备注信息",
                                field: "remarks",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return data;
                                    }
                                },
                                width: 10
                            },
                            {
                                title: "领取时间",
                                field: "receiveTime",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    }else {
                                        return DateTimeFormatter(data);
                                    }
                                },
                                width: 18

                            }, {
                                title: "开始时间",
                                field: "startTime",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return DateTimeFormatter(data);
                                    }
                                },
                                width: 18
                            }, {
                                title: "结束时间",
                                field: "endTime",
                                formatter: function (data) {
                                    if (data == null) {
                                        return "-";
                                    } else {
                                        return DateTimeFormatter(data);
                                    }
                                },
                                width: 18
                            },
                            {
                                title: "操作",
                                field: "operate",
                                formatter: function (data, rowData) {
                                    console.log(rowData.status);
                                    if (data == null) {
                                        var result = "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='查看' type='button' onclick='jobInfo(" + JSON.stringify(rowData) + ")'/>";
                                        var ujId = rowData.examineId;
                                        if(1 == rowData.status){
                                            if(null != ujId &&'' != ujId){//
                                                result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='审核' type='button' onclick='userJobList(" + JSON.stringify(rowData) + ")'/>";
                                            }
                                        }else if(2 == rowData.status){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='完成' type='button' onclick='finishJob(" + JSON.stringify(rowData) + ")'/>";
                                        }else if(4 == rowData.status){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='重新上架' type='button' onclick='reshelfJob(" + JSON.stringify(rowData) + ")'/>";
                                        }
                                        if(rowData.status == 0){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='下架' type='button' onclick='closeJob(" + JSON.stringify(rowData) + ")'/>";
                                        }
                                        if(5 != rowData.status){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='修改' type='button' onclick='openAddDiv(false," + JSON.stringify(rowData) + ")'/>";
                                        }
                                        if(0 == rowData.status || 1 == rowData.status){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='推送' type='button' onclick='sendJob(" + JSON.stringify(rowData) + ")'/>";
                                        }
                                        if(1 == rowData.examineStatus){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='审核通过' type='button''/>";
                                        }
                                        if(2 == rowData.examineStatus){
                                            result += "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='审核不通过' type='button''/>";
                                        }

                                        return result;
                                    } else {
                                        return "";
                                    }
                                },
                                width: 40
                            }
                        ]]
                    }
            )
            ;
        }


        var isSubmitAdd;
        function openAddDiv(isAdd,data) {
            isSubmitAdd = isAdd;
            if (isAdd) {

                $('#addDiv').window({title: "增加"});
                $('#addDiv').window('open');  // open a window
                $('#addDiv').window('center');


                var inputs = $("#addForm input.easyui-textbox");
                inputs.textbox("setText", "");
                inputs.textbox("setValue", "");
                var timebox = $("#addForm input.easyui-datetimebox");
                timebox.textbox("setText", "");
                timebox.textbox("setValue", "");
                $("#add_price").val("");

            } else {

                console.log(data.startTime);
                console.log(data.endTime);


                $('#addDiv').window({title: "修改"});
                $('#addDiv').window('open');  // open a window
                $('#addDiv').window('center');


                $('#add_jid').val(data.jid);
                $('#add_status').val(data.status);


                $('#add_name').textbox('setText',data.name);
                $('#add_name').textbox('setValue',data.name);

                $('#add_subhead').textbox('setText',data.subhead);
                $('#add_subhead').textbox('setValue',data.subhead);

                $('#add_description').val(data.description);

                $('#add_price').val(data.price);

                $('#add_tags').val(data.tags);

                $('#add_address').val(data.address);

                $('#add_startTime').datetimebox('setText',DateTimeFormatter(data.startTime));
                $('#add_startTime').datetimebox('setValue',DateTimeFormatter(data.startTime));

                $('#add_endTime').datetimebox('setText',DateTimeFormatter(data.endTime));
                $('#add_endTime').datetimebox('setValue',DateTimeFormatter(data.endTime));
            }
        }

        function addJob() {

            var name = $('#add_name').textbox('getValue');
            if (null  == name || '' == name) {
                $.messager.confirm("提示", "请输入任务名称！");
                return false;
            }

            var name = $('#add_name').textbox('getValue');
            if (null  == name || '' == name) {
                $.messager.confirm("提示", "请输入任务名称！");
                return false;
            }

            var employerName = $('#add_employerName').textbox('getValue');
            if (null  == employerName || '' == employerName) {
                $.messager.confirm("提示", "请输入雇主名称！");
                return false;
            }

            var employerMobile = $('#add_employerMobile').textbox('getValue');
            if (null  == employerMobile || '' == employerMobile) {
                $.messager.confirm("提示", "请输入雇主联系方式！");
                return false;
            }

            var address = $('#add_address').val();
            if (null  == address || '' == address) {
                $.messager.confirm("提示", "请输入服务地址！");
                return false;
            }


            var startTime = $('#add_startTime').datetimebox('getValue');;
            if (null  == startTime || '' == startTime) {
                $.messager.confirm("提示", "请选择任务开始时间！");
                return false;
            }

            var endTime = $('#add_endTime').datetimebox('getValue');
            if (null  == endTime || '' == endTime) {
                $.messager.confirm("提示", "请选择任务结束时间！");
                return false;
            }

            if (new Date(startTime.replace(/\-/g, '\/')) > new Date(endTime.replace(/\-/g, '\/'))) { //开始时间大于了结束时间
                $.messager.confirm("提示","时间选择有误！开始日期必须早于结束时期！");
                return false;
            }

            $('#addDiv').window('close');

            if (isSubmitAdd) {
                $("#addForm").attr("action", "/job/addJob");
                $("#addForm").ajaxSubmit(function (resultJson) {
                    //回调操作
                    $.messager.confirm("提示", resultJson.msg);
                    $("#job").datagrid("reload");
                });
            } else {
                $("#addForm").attr("action", "/job/editJob");
                $("#addForm").ajaxSubmit(function (resultJson) {
                    //回调操作
                    $.messager.confirm("提示", resultJson.msg);
                    $("#job").datagrid("reload");
                });

            }
        }

        function closeJob(data) {
            console.log(data);
            var ujid = data.examineId;
            var jid = data.jid;
            $.messager.confirm('提示', '确定要下架这条任务吗？', function (r) {
                if (r) {
                    $.ajax({
                        url: "/job/operationJob",
                        dataType: "json",
                        type: "post",
                        data: {
                            jid: jid,
                            type:'close',
                            ujid:ujid
                        },
                        error: function () {
                            alert("系统错误!");
                        },
                        success: function (data) {
                            $.messager.confirm("提示", data.msg);
                            $("#job").datagrid("reload");
                        }
                    });
                }
            });
        }

        function reshelfJob(data){
            var jid = data.jid;
            $.messager.confirm('提示', '确定要重新上架吗？', function (r) {
                if (r) {
                    $.ajax({
                        url: "/job/operationJob",
                        dataType: "json",
                        type: "post",
                        data: {
                            jid: jid,
                            type:'reshelf'
                        },
                        error: function () {
                            alert("系统错误!");
                        },
                        success: function (data) {
                            $.messager.confirm("提示", data.msg);
                            $("#job").datagrid("reload");
                        }
                    });
                }
            });
        }

        function finishJob(data){
            var jid = data.jid;
            $.messager.confirm('提示', '确定要完成任务吗？', function (r) {
                if (r) {
                    $.ajax({
                        url: "/job/operationJob",
                        dataType: "json",
                        type: "post",
                        data: {
                            jid: jid,
                            type:'finish'
                        },
                        error: function () {
                            alert("系统错误!");
                        },
                        success: function (data) {
                            $.messager.confirm("提示", data.msg);
                            $("#job").datagrid("reload");
                        }
                    });
                }
            });
        }

        function cancelAddDiv() {
            $('#addDiv').window('close');
        }

        function cancelExamineDiv() {
            $('#examineDiv').window('close');
        }

        function openExamineDiv(data) {

            $('#add_examineStatus').combobox('setValue','1');
            $("#add_examineRemark").val("");
            $("#examine_id").val(data.examineId);

            $('#examineDiv').window({title: "审核"});
            $('#examineDiv').window('open');  // open a window
            $('#examineDiv').window('center');

        }

        function sendJob(data) {
            var jid = data.jid;
            $.messager.confirm('提示', '确定要推送该任务吗？', function (r) {
                if (r) {
                    $.ajax({
                        url: "/job/sengMassage",
                        dataType: "json",
                        type: "post",
                        data: {
                            title:"任务推送",
                            content: "出新任务啦，要不要看看呢",
                            type:'3',
                            sn:jid
                        },
                        error: function () {
                            alert("系统错误!");
                        },
                        success: function (data) {
                            $.messager.confirm("提示", data.msg);
                            $("#job").datagrid("reload");
                        }
                    });
                }
            });
        }

        function addExamine(){

            $("#examineForm").attr("action", "/userJob/examineUserJob");
            $("#examineForm").ajaxSubmit(function (resultJson) {
                //回调操作
                $.messager.confirm("提示", resultJson.msg);
                $("#job").datagrid("reload");
                $('#examineDiv').window('close');
            });
        }

        function jobInfo(data){

            $('#jobInfo').window('open');
            var statuStr = '';
            var examineStatusStr = '';
            switch (data.status){
                case 1:
                    statuStr = '待审核';
                    examineStatusStr = '待审核';
                    break;
                case 2:
                    statuStr = '审核通过';
                    examineStatusStr = '审核通过';
                    break;
                case 4:
                    statuStr = '任务下架';
                    break;
                case 5:
                    statuStr = '任务完成';
                    examineStatusStr = '审核通过';
                    break;
                default :
                    statuStr = '待领取';
                    break;
            }

            var html = '<table  style="height:90%;width:100%;">'
                    +  '<tr>'
                    +   '<td>任务编号：'+ (null == data.jid ? '' :data.jid) +'</td>'
                    +   '<td>任务名称：'+ (null == data.name ? '' : data.name)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td colspan=2>副标题：'+ (null == data.subhead ? '' : data.subhead) +'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td>标签：'+ (null == data.tags ? '' : data.tags)+'</td>'
                    +   '<td>状态：'+ statuStr+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td colspan=2>内容：'+ (null == data.description ? '' : data.description) +'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td colspan=2>雇主姓名：'+ (null == data.employerName ? '' : data.employerName)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td colspan=2>联系方式：'+ (null == data.employerMobile ? '' : data.employerMobile)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td colspan=2>地址：'+ (null == data.address ? '' : data.address)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td>开始时间：'+ DateTimeFormatter(data.startTime)+'</td>'
                    +   '<td>结束时间：'+ DateTimeFormatter(data.endTime)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td colspan=2> <<< 任务领取详情 >>> </td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td>领取人：'+ (null == data.receiveName ? '' : data.receiveName)+'</td>'
                    +   '<td>领取时间：'+ (null == data.receiveTime ? '' : DateTimeFormatter(data.receiveTime))+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td>领取人手机：'+ (null == data.receiveMobile ? '' : data.receiveMobile)+'</td>'
                    +   '<td>领取人身份证：'+ (null == data.receiveCardNo ? '' : data.receiveCardNo)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td>领取人备注信息：'+ (null == data.remarks ? '' : data.remarks)+'</td>'
                    +  '</tr>'
                    +  '<tr>'
                    +   '<td>审核状态：'+ examineStatusStr+'</td>'
                    +   '<td>审核备注：'+ (null == data.examineRemark ? '' : data.examineRemark) +'</td>'
                    +  '</tr>'
                    + '</table>';
            $("#info_div").html(html);
        }

        function searchJob() {

            var jid = $("#search_jid").textbox("getValue");
            var name = $("#search_name").textbox("getValue");
            var status = $("#search_status").combobox("getValue");

            $("#job").datagrid({
                url: "/job/listJob",
                queryParams: {
                    jid: jid,
                    name: name,
                    status: status
                }
            });

        }

        /**人员审核列表*/
        function userJobList(data){
            $('#userjob').window('open');  // open a window
            $('#userjob').window('center');
            var jid = data.jid;

            $("#userjob2").datagrid({
                    url: "/job/getJobUserList",
                    method: "post",
                    idField: "jid",
                    remoteSort: true,
                    fitColumns: true,
                    fit: true,
                    striped: true,
                    pagination: true,
                    rownumbers: true,
                    singleSelect: true,
                    autoRowHeight: true,
                    pageSize: 20,
                    pageList: [10, 15, 20, 25, 30],
                    queryParams:{
                        jid:jid
                    },
                    frozenColumns: [[
                        {
                            title: "编号",
                            field: "jobId",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else {
                                    return data;
                                }
                            },
                            width: 100
                        },
                        {
                            title: "用户编号",
                            field: "uid",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else {
                                    return data;
                                }
                            },
                            width: 100
                        }
                        ]],
                    columns: [[
                        {
                            title: "领取人",
                            field: "name",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else {
                                    return data;
                                }
                            },
                            width: 20
                        },{
                            title: "备注信息",
                            field: "remark",
                            formatter: function (data) {
                                if (data == null) {
                                    return "-";
                                } else {
                                    return data;
                                }
                            },
                            width: 15
                        },
                        {
                            title: "操作",
                            field: "operate",
                            formatter: function (data, rowData) {
                                console.log(rowData.status);
                                if (data == null) {
                                    var result = "<input style='background:-moz-linear-gradient(top,#ffffff,#eeeeee);border-radius: 4px;border:1px #cccccc solid;' value='审核' type='button' onclick='openExamineDiv(" + JSON.stringify(rowData) + ")'/>";
                                    return result;
                                } else {
                                    return "";
                                }
                            },
                            width: 40
                        }
                    ]]
                }
            )
        }

    </script>

</head>
<body>

<div id="toolBar">
    <a class="easyui-linkbutton" onclick="openAddDiv(true)" data-options="iconCls:'icon-add'"
       style="margin:5px 5px 5px 0px;">新增</a>
    <%--<a class="easyui-linkbutton" onclick="openAddDiv(false,this)" data-options="iconCls:'icon-edit'"--%>
    <%--style="margin:5px 5px 5px 0px;">修改</a>--%>
    任务状态：
    <select id="search_status" class="easyui-combobox" name="status" style="width: 145px;">
        <option value="">全部</option>
        <option value="0">待领取</option>
        <option value="1">待审核</option>
        <option value="2">审核通过</option>
        <option value="4">已下架</option>
        <option value="5">任务完成</option>
    </select>
    编号：
    <input class="easyui-textbox" id="search_jid" name="jid" type="text"/>
    任务名称：
    <input class="easyui-textbox" id="search_name" name="name" type="text"/>
    <a class="easyui-linkbutton" onclick="searchJob()" data-options="iconCls:'icon-search'"
       style="margin:5px 5px 5px 0px;">查询</a>
    <br/>
</div>

<table id="job" style="width:auto;"></table>



<div id="jobInfo" class="easyui-window" title="查看任务详情" style="height:500px;width:600px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <div class="easyui-layout" data-options="fit:true" id="info_div">

    </div>
</div>

<div id="userjob" class="easyui-window" title="审核人员任务" style="height:500px;width:600px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <table id="userjob2" style="width:auto;"></table>
</div>

<div id="addDiv" class="easyui-window" title="添加任务" style="height:600px;width:400px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" style="height:90%;">
            <form id="addForm" method="POST">
                <input id="add_jid" name="jid" type="hidden"/>
                <table style="text-align: center;width: 100%;height:90%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td>任务名称:</td>
                        <td><input class="easyui-textbox" id="add_name" name="name" type="text"  style="width: 80%;"/></td>
                    </tr>
                    <tr>
                        <td>副标题:</td>
                        <td><input class="easyui-textbox" id="add_subhead" name="subhead" type="text" style="width: 80%;"/></td>
                    </tr>
                    <tr>
                        <td>价格:</td>
                        <td><input type="text" id="add_price"  name="price" type="text"
                                   onkeyup="clearNoNum(this)" style="width: 80%;" /></td>
                    </tr>
                    <tr>
                        <td>标签:</td>
                        <td>
                            <textarea id="add_tags" name="tags" type="text" style="width: 80%;height: 50px;resize:none" ></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>雇主姓名:</td>
                        <td>
                            <input class="easyui-textbox" id="add_employerName" name="employerName" type="text" style="width: 80%;" />
                        </td>
                    </tr>
                    <tr>
                        <td>联系方式:</td>
                        <td>
                            <input class="easyui-textbox" id="add_employerMobile" name="employerMobile" type="text" style="width: 80%;" />
                        </td>
                    </tr>
                    <tr>
                        <td>地址:</td>
                        <td>
                            <textarea id="add_address" name="address" type="text" style="width: 80%;height: 50px;resize:none" ></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>开始时间:</td>
                        <td><input class="easyui-datetimebox" id="add_startTime" name="startTime" type="text" style="width: 80%;"/></td>
                    </tr>
                    <tr>
                        <td>结束时间:</td>
                        <td><input class="easyui-datetimebox" id="add_endTime" name="endTime" type="text" style="width: 80%;"/></td>
                    </tr>
                    <tr>
                        <td>内容</td>
                        <td>
                            <textarea id="add_description" name="description" type="text" style="width:80%;height: 100px;resize:none" ></textarea>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'" style="text-align:center;">
            <a class="easyui-linkbutton" onclick="addJob()" data-options="iconCls:'icon-ok'"
               style="margin: 5px">确定</a>
            <a class="easyui-linkbutton" onclick="cancelAddDiv()" data-options="iconCls:'icon-cancel'"
               style="margin: 5px">取消</a>
        </div>
    </div>
</div>

<div id="examineDiv" class="easyui-window" title="审核" style="height:300px;width:400px;"
     data-options="iconCls:'icon-search',modal:true" closed="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" style="height:80%;">
            <form id="examineForm" method="POST">
                <table style="text-align: center;width: 100%;height:90%" border="0" cellpadding="2" cellspacing="0">
                    <input id="examine_id" name="examine_id" hidden="hidden"/>
                    <tr>
                        <td>是否通过:</td>
                        <td><select class="easyui-combobox" id="add_examineStatus" name="examineStatus" style="width:80%">
                            <option value="1" selected="selected">通过</option>
                            <option value="2">不通过</option>
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td>备注:<br/>(不通过理由)</td>
                        <td>
                            <textarea id="add_examineRemark" name="examineRemark" type="text" style="width: 80%;height: 100px;resize:none" ></textarea>
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
