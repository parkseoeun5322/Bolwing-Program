<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>COMIN BOWLING</title>
		<!-- <link rel="icon" type="image/x-icon" href="img/main_logo.png"> -->	<!-- 타이틀 바의 아이콘 설정 -->
		<!-- <link rel="stylesheet" type="text/css" href="css/basic.css"> -->
		<!-- fontawesome을 사용하기 위해 라이브러리 추가 -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.14.0/js/all.min.js"></script>
		<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
		<!-- 카카오 Map API  -->
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=db4bd5c7848b54b4a6c6bfa8d6b67065&libraries=services"></script>
		<!-- Bootstrap 4 -->
		<script src="resources/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
		<!-- SlimScroll -->
		<script src="resources/plugins/slimScroll/jquery.slimscroll.min.js"></script>
		<!-- FastClick -->
		<script src="resources/plugins/fastclick/fastclick.js"></script>
		<!-- AdminLTE App -->
		<script src="resources/dist/js/adminlte.min.js"></script>
		<!-- AdminLTE for demo purposes -->
		<script src="resources/dist/js/demo.js"></script>
		 <!-- Font Awesome -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<!-- Ionicons -->
		<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
		<!-- Theme style -->
		<link rel="stylesheet" href="resources/dist/css/adminlte.min.css">
		<!-- Google Font: Source Sans Pro -->
		<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
	</head>
<body>
	<tiles:insertAttribute name="header"/>
	<div id="content">
		<tiles:insertAttribute name="content"/>
	</div>
	<tiles:insertAttribute name="footer"/>
</body>
</html>
