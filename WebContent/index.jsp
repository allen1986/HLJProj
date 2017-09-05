<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件列表</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	//上传进度句柄	
	var intervalId;
	$(function() {
		$('#dg').datagrid({
			url : 'filelist'
		});
		$('#dd').dialog({
			'closed' : true
		});

	});
	//删除文件
	function fileDelete(id) {
		$.messager.confirm('消息提示', '确定要删除该文件吗?', function(r) {
			if (r) {
				$.ajax({
					url : "${pageContext.request.contextPath}/delete?id=" + id,
					type : 'post',
					success : function() {
						//刷新界面
						console.log('success');
						$("#dg").datagrid('reload');
					},
					error : function(data) {
						console.log("删除文件错误，原因：", data);
					}
				})
			}
		});
	}
	function formatOper(value, row, index) {
		return '<a href="${pageContext.request.contextPath}/download?fileName='
				+ row['fileName']
				+ '" name="opera" class="easyui-linkbutton" >下载</a>  |  <a href="#" onclick="fileDelete('
				+ row['id']
				+ ')" name="opera" class="easyui-linkbutton" >删除</a>';
	}
	//弹出文件上传对话框
	function popUploadDlg() {
		$("#dd").dialog('open');
	}
	//文件上传
	function upload() {
		var formData = new FormData($("#fileForm")[0]);
		$.ajax({
			url : 'upload',
			type : 'post',
			data : formData,
			contentType : false,
			processData : false,
			success : function(data) {
				//下载完成，隐藏对话框，进度条，刷新列表
				$('#dd').dialog('close');
				$('#filename').val("");
				$("#progressbar").css("display", "none");
				$("#dg").datagrid('reload');
			}
		});
		//更新上传进度
		$("#progressbar").css("display", "block");
		intervalId = setInterval(uploadProgress, 300);
	}
	//更新上传进度
	function uploadProgress() {
		$.ajax({
			url : 'progress',
			dataType : 'json',
			success : function(data) {
				$('#progressbar').progressbar({
					value : data
				});
				if (data == 100) {
					window.clearInterval(intervalId);
				}
			}
		});
	}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center'" style="padding: 10px">
			<div id="toolbar" style="float: right; margin-right: 20px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-upload"
					plain="true" onclick="popUploadDlg()">上传文件</a>
			</div>
			<table id="dg" title="文件列表" style="width: 100; height: 100"
				toolbar="#toolbar" idField="id" rownumbers="true" fitColumns="true"
				pagination="true">
				<thead>
					<tr>
						<th field="fileName" width="70"
							editor="{type:'validatebox',options:{required:true}}">文件名</th>
						<th field="creatorName" width="10"
							editor="{type:'validatebox',options:{required:true}}">创建者</th>
						<th field="createTime" width="10"
							editor="{type:'validatebox',options:{required:true}}">创建时间</th>
						<th
							data-options="field:'_operate',width:15,align:'center',formatter:formatOper">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!-- 对话框 -->
	<div id="dd" class="easyui-dialog" title="文件上传"
		style="width: 400px; height: 150px; padding: 10px"
		buttons="#dlg-buttons">
		<form id="fileForm" enctype="multipart/form-data">
			<input type="file" name="filename" id="filename" />
			<div id="progressbar" class="easyui-progressbar"
				data-options="value:0"
				style="display: none; width: 100; margin-top: 10px"></div>
		</form>
	</div>
	<div id="dlg-buttons">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td style="text-align: center"><a href="#"
					class="easyui-linkbutton" iconCls="icon-save" onclick="upload()">上传</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
					onclick="javascript:$('#dd').dialog('close')">取消</a></td>
			</tr>
		</table>
	</div>
</body>
</html>