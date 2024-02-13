<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../../../common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta name="generator" content="HTML Tidy for HTML5 (experimental) for Windows https://github.com/w3c/tidy-html5/tree/c63cc39" />
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
    <div>
       <video id="video_object" class="video-js vjs-default-skin vjs-big-play-centered" controls preload="auto" 
       	style="width:100%;height:100%;" data-setup='{"controls": true, "autoplay": false, "preload": "auto"}'>
       	   <source src="/files/video/getFileBytes.do?fid=${fid}" type='video/${type}' />
       </video>
    </div>
</div>
  <script type="text/javascript">
      videojs.options.flash.swf = "video-js.swf";
      var myPlayer = videojs('video_object');
      videojs("video_object").ready(function(){
      	var myPlayer = this;
      	myPlayer.play();
      });
  </script>

</body>
</html>