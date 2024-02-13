<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/> 
<title>UploadFileForm</title>
<%--<link rel="stylesheet" type="text/css" href="/static/uploadify/uploadify.css" />
<script type="text/javascript" src="/static/uploadify/jquery.uploadify.js"></script>--%>
<script type="text/javascript" src="/static/uploadAdapter/jquery.loadscript.js"></script>
<script type="text/javascript" src="/static/uploadAdapter/jquery.uploadAdapter.js"></script>
<script type="text/javascript" src="/static/ace/js/bootstrap.js"></script>
<script type="text/javascript" src="/static/ace/js/bootbox.js"></script>
<link rel="stylesheet" href="/static/ace/css/bootstrap.css" />
<link rel="stylesheet" href="/static/ace/css/font-awesome.css" />

<script type="text/javascript">
	$(function() {
	   /*$("#file_upload").uploadify({
	    	'debug'    : false,
	    	'height'   : 30,//按钮的高度
            'width'    : 100,//按钮的宽度
	        'swf'      : '/static/uploadify/uploadify.swf',//swf文件
	        'uploader' : '/files/uploadFiles',//指向后台操作的action，可以带参数
	        'buttonImage' : '/static/uploadify/uploadify-upload.png',//浏览文件按钮图片
	        'queueID'  : 'fileQueue',//绑定显示上传队列的div
	        'fileTypeExts'  : '*.*',//限制文件上传的类型
	        'method'   : 'post',//限制文件上传的类型
	        'post_params' : {
				'id' : '${id}',  // row key id
				'columnsId' : '${columnsId}',
				'columnsValue' : '${columnsValue}',
				'tablesId'  : '${tablesId}',
				'topCorpBianma' : '${topCorpBianma}'
	        },
	        'auto'     : false,//是否自动上传
	        'multi'    : false,//是否进行多文件上传
	        'fileSizeLimit' : "100 MB", // 根据用户收费级别 10M/50M/100M TODO:
	        'queueSizeLimit' : 1,
	        'fileObjName': 'file',//后台接收表单控件的名字，必须与后台名称保持一致，相当与input type=file的name属性
	        'successTimeout': 10, //后台上传的超时时间，如果超过60秒，onUploadSuccess将返回response:false
	        'onUploadSuccess' : function(file, data, response) {//每个上传完成并成功的文件都会触发本事件
	        	if (response == false && data == '') {
	        		bootbox.alert('由于文件太大或者网速较慢导致1分钟内没有上传完成，不需要再次点击上传按钮，系统将转为后台上传模式，请稍候点击刷新按钮查看是否上传成功。');
	        		return;
	        	}
        		var data = eval("("+data+")");
        		if (!data.success) {
        			bootbox.alert("文件【" + file.name + "】上传失败");
        		} else {
            		// 刷新datagrid
            		// 上传成功之后，刷新slickgrid的当前行的文件列的值
            		$("#UPLOAD_FILE_NAME").val(data.msg);
        		}
    		},
        	'onQueueComplete' : function(queueData){//队列全部上传触发函数
        		//提示信息
        		//bootbox.alert("【" + queueData.uploadsSuccessful + "】个文件上传成功");
        		//bootbox.alert(queueData.files.SWFUpload_0_0.name);
        		//成功关闭页面
        		//$("#UPLOAD_FILE_NAME").val(queueData.files.SWFUpload_0_0.name);
        		//closeWinPopup();	
        	},
        	'onFallback': function(){ //检测FLASH失败调用
          		bootbox.alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试!");
      		},
      		'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
                //有时候上传进度什么想自己个性化控制，可以利用这个方法
                //使用方法见官方说明
            },
            //选择上传文件后调用
            'onSelect' : function(file) {
            	//$('#uploadify').uploadifyClearQueue();
            },
            //返回一个错误，选择文件的时候触发
            'onSelectError':function(file, errorCode, errorMsg){
                switch(errorCode) {
                    case -100:
                    	this.queueData.errorMsg = "上传的文件数量已经超出系统限制的"+$('#file_upload').uploadify('settings','queueSizeLimit')+"个文件！";
                        break;
                    case -110:
                    	this.queueData.errorMsg = "文件 ["+file.name+"] 大小超出系统限制的"+$('#file_upload').uploadify('settings','fileSizeLimit')+"大小！";
                        break;
                    case -120:
                    	this.queueData.errorMsg = "文件 ["+file.name+"] 大小异常！";
                        break;
                    case -130:
                    	this.queueData.errorMsg = "文件 ["+file.name+"] 类型不正确！";
                        break;
                }
            },
	    });*/


	    $("#upload").click(function(){
	    	//$("#file_upload").uploadify("settings", "formData", {'userName':name,'qq':qq});
            var formData = new FormData($('#uploadForm')[0]);
            $.ajax({
                type: 'post',
                url: "/files/uploadFiles",
                post_params: {
                    'id' : '${id}',  // row key id
                    'columnsId' : '${columnsId}',
                    'columnsValue' : '${columnsValue}',
                    'tablesId'  : '${tablesId}',
                    'topCorpBianma' : '${topCorpBianma}'
                },
                data: formData,
                cache: false,
                processData: false,
                contentType: false,
            }).success(function (data) {
                if(data.success){
                    bootbox.alert('上传成功');
				}else{
                    $('#file_upload').uploadify('upload', '*');
				}

            }).error(function () {
                $('#file_upload').uploadify('upload', '*');
            });


	    });
	    
	    $("#cancel").click(function(){
	    	$('#file_upload').uploadify('cancel', '*');
	    });
	    
	    $("#close").click(function(){
	    	top.Dialog.close();
	    });

        $('#file_upload').uploadAdapter({
            auto:true,
            buttonText:'选择文件',
            fileObjName:'file',
            fileTypeExts:'*.*',
            multi:false,
            formData:{id: '${id}',  // row key id
                columnsId: '${columnsId}',
                columnsValue: '${columnsValue}',
                tablesId : '${tablesId}',
                topCorpBianma: '${topCorpBianma}'},
            fileSizeLimit:100*1024*1024,
            showUploadedPercent:true,
            showUploadedSize:true,
            removeTimeout: 1,//上传完成后进度条的消失时间
            uploader:'/files/uploadFiles',
            onUploadSuccess: function (file, data, response) {
				if (response == false && data == '') {
					bootbox.alert('由于文件太大或者网速较慢导致1分钟内没有上传完成，不需要再次点击上传按钮，系统将转为后台上传模式，请稍候点击刷新按钮查看是否上传成功。');
					return;
				}
				var data = eval("("+data+")");
				if (!data.success) {
					bootbox.alert("文件【" + file.name + "】上传失败");
				} else {
					// 刷新datagrid
					// 上传成功之后，刷新slickgrid的当前行的文件列的值
					$("#UPLOAD_FILE_NAME").val(data.msg);
					bootbox.alert("上传成功!");
				}
            }
        });

	});





</script>
</head>
<body STYLE="BACKGROUND-COLOR:WHITE">
	<div class="main-container" id="main-container">
		<input type="hidden" name="UPLOAD_FILE_NAME" id="UPLOAD_FILE_NAME" value=""/>
		<div region="center" border="false" split="false" id="main" style="overflow:auto;padding: 2px;">
            <div id="file_upload" style="width:100%;"></div>
		   <%-- <div id="fileQueue" style="margin-left: 10px; margin-top: 10px; margin-bottom: 10px; float: none; clear: both; font-size: 12px !important; line-height: 15px !important;">
		    </div>--%>
		</div>

	   <%-- <div region="south" border="false" split="false" style="height:30px; line-height:25px; padding-top:2px; text-align:center;background: #F3F3F3;">
			<a href="javascript:void(0)" id="upload" class="easyui-linkbutton" data-options="iconCls:'icon-up'">开始上传</a>
            &lt;%&ndash;<a href="javascript:void(0)" id="cancel" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消上传</a>
            <a href="javascript:void(0)" id="close" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>&ndash;%&gt;
		</div>--%>
		<div style="margin-top: 20px">
			<h5>由于文件上传占用大量带宽和存储，如果您有上传大文件（>100MB）的需求，需要单独为您定制；请联系我们，QQ群:<font color="#FF0000">609281718</font></h5>
		</div>
	</div>
</body>
</html>
