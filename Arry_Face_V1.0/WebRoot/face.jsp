<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>实时刷脸门禁信息门禁系统 - arry老师</title>
		<meta name="Keywords" content="关键词,关键词">
		<meta name="description" content="">

		<style type="text/css">
			*{margin:0;padding:0;}
			body{font-size:12px;font-family:"微软雅黑";color:#666;}

			/*h1 start*/
			h1{text-align:center;line-height:80px;font-size:30px;font-weight:300;}
			/*end h1*/

			/*face start*/
			.face{width:600px;height:400px;margin:0 auto;position:relative;overflow:hidden;}
			.face #canvas{display:none;}
			/*end face*/

			/*btn start*/
			.btn{width:300px;height:46px;display:block;background:#6CF;margin:20px auto;text-align:center;line-height:46px;text-decoration:none;font-size:14px;color:#fff;border-radius:23px;}
			.btn:hover{background:#66F;}
			/*end btn*/

			.f-scan{width:100%;height:100%;background:url("images/intro.png") no-repeat;position:absolute;background-size:cover;}
			#message{text-decoration:center;font-size:30px;color:red;line-height:60px;text-align:center;}

		</style>

	</head>
<body>

<!--h1 start-->
<h1>实时刷脸门禁信息门禁系统</h1>
<!--end h1-->

<div id="message"></div>

<!--face start-->
<div class="face" id="face">
	<video id="video" width="600" height="400"></video>
	<canvas id="canvas" width="600" height="400"></canvas>
	<div class="f-scan"></div>
</div>
<!--end face-->

<!--btn start-->
<a href="#" class="btn">人工智能刷脸门禁系统</a>
<!--end btn-->


<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
	
	$(function(){
		
		// 初始化系统摄像头
		init_getvideo();

		$(".btn").click(function(){
			
			// 刷脸
			scan();
			
			var video = document.getElementById("video");
			var context = canvas.getContext("2d");
			context.drawImage(video,0,0,600,400);
			// 获取canvas摄像头的画面截图程编码为base64的加密字符串
			var imgSrc = document.getElementById("canvas").toDataURL('image/png');
			var base64 = imgSrc.split("base64,")[1];
			
			
			$.ajax({
				type:"post",
				url:"data.jsp",
				data:{"imgBase64":base64},
				success:function(data){
					data = eval(data);
					if(data){
						window.location.href = "http://www.itstaredu.com/";
					} else {
						$("#message").html("你长得太丑了，要换一个跟arry老师一样帅的男神才可以登录 ！");
					}
				}
				
			});
			
		});

	
	});

	// 初始化系统摄像头
	function init_getvideo(){
		// navigator 浏览器的内置对象
		var getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
		
		getUserMedia.call(navigator,{
			video:true,
			audio:false
		},function(localMediaStream){
			var video = document.getElementById('video');
			video.src = window.URL.createObjectURL(localMediaStream);
			video.onloadedmetadata = function(e){
				video.play();
			}
		},function(e){
			console.log('太棒了 !',e);
		}
				
		);

	}


	// 图像扫描
	function scan(){
		var box = $("#face"); 
		$(".f-scan").css({"bottom":box.height()}).animate({bottom:0},2000,function(){
			$(this).css({"bottom":box.height()});
		});
	}

</script>

</body>
</html>