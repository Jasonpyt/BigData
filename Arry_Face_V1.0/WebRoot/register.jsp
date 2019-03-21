<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
 		<style type="text/css">
 			*{margin: 0;padding: 0;}
 			body{height: 100vh;background: url(images/bg.jpg)no-repeat center;background-size: cover;}
 			h1{text-align: center;font-weight: 100;height: 100px;line-height: 100px;color: #000;}
 			.message{color: red;}
 			#canvas{display: none;}
 			#media{width: 600px;height: 400px;margin:0 auto 0;position: relative;overflow: hidden;border-radius: 14px;}
 			.sm{width: 100%;height: 100%;background: url(images/intro.png)no-repeat;position: absolute;background-size: cover;}
 			#btn{width: 250px;height: 46px;margin: 20px auto 0;text-align: center;line-height: 46px;background: #00f1ff;color: #fff;cursor: pointer;border-radius: 40px;}
 		</style>	
    </head>
 
    <body>
    	<h1>实时刷脸门禁信息录入系统</h1>
        <div id="media">
        	<video id="video" width="600" height="400" ></video>
			<canvas id="canvas" width="600" height="400"></canvas>
			<div class="sm"></div>
        </div>
        <form action="register" method="post" id="from">
        	<input type="hidden" value="" name="imgBase64" id="imgBase64" />
        </form>
        <div id="btn" onclick="register()">人工智能刷脸录入系统</div>
    </body>
    <script type="text/javascript" src="js/jquery-3.1.1.min.js" ></script>
    <script type="text/javascript">
    	var video = document.getElementById("video");
		var context = canvas.getContext("2d");
      	var getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);   
	    //navigator	浏览器内置对象
	    getUserMedia.call(navigator, {//Navigator 对象包含的属性描述了正在使用的浏览器。可以使用这些属性进行平台专用的配置
	        video: true,   
	        audio: false   
	    }, function(localMediaStream) {//Blob对象可以看做是存放二进制数据的容器
	        var video = document.getElementById('video'); //获取video标签对象  
	        video.src = window.URL.createObjectURL(localMediaStream);//将摄像头的流媒体赋值给video标签的src
	        video.onloadedmetadata = function(e) {
		        video.play();
	        };
	    }, function(e) {
	        console.log('调用摄像头出错!', e);
	    });
      	//navigator领航员;航海家，航行者，航海者Devices设备media媒体
      	/* navigator.mediaDevices.getUserMedia({
      			audio:false,
      			video:{facingMode:"user"}
      		})
      		.then(function(stream){
	      		var video = document.querySelector('video');
	      		video.src = window.URL.createObjectURL(stream);
	      		video.onloadedmetadata = function(e){//metadata元数据
	      			video.play();
	      		}
      	}).catch(function(err){console.log(err.name+":"+err.message)}) */
      	
      	function register(){
      		sm();
      		/*context.drawImage(img,x,y,width,height);*/
      		context.drawImage(video,0,0,600,400)
      		var base64 = getBase64();
      		$("#imgBase64").val(base64);
			$("#from").submit();
      	}
      	
      	function getBase64(){
      		var imgSrc = document.getElementById('canvas').toDataURL('image/png');
      		var base64 = imgSrc.split("base64,")[1];
      		return base64;
      	}
      	
      	//图片扫描
		function sm(){
			var box = $("#media");
			$(".sm").css({"bottom":box.height()}).animate({bottom:0},2000,function(){$(this).css({"bottom":box.height()})});
		}
    </script>
 
</html>