<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../../../common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="x-dns-prefetch-control" content="on" />

  <title>video</title>
  <meta name="renderer" content="webkit" />

  <link href="/static/videojs/video-js.min.css" type="text/css" rel="stylesheet" />

  <style>
    .j-videobox {
	  background-color: black;
    }
  </style>
  <script type="text/javascript" src="/static/videojs/video.min.js"></script>
</head>
<body style="margin:0;padding:0;">

<div class="j-videobox">
    <div id="viewDiv">
        <iframe id="iframePreview" width='100%' height='700' ></iframe>
    </div>
</div>
  <script type="text/javascript">
  $(function(){
	  var fileUrl = "/files/pdf/getFileBytes.do?fid=${fid}";
	  var tmpUrl = '/web/viewer.html?file=' + encodeURIComponent(fileUrl);
      $("#iframePreview").attr("src", tmpUrl);
  });
  </script>

</body>
</html>