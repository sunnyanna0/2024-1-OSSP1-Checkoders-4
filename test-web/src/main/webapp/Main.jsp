<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
    <head>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>여행 짜조.com</title>
        <link href="main.css" rel="stylesheet" />
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
        <script src="kakao_login_logout.js"></script>
        <script src="checkIsLogin.js"></script>
    </head>
    <body>
    <span class="string1">여행 스케줄러</span>
    <span class="string2">효율적인 여행을 위한 선택!!</span>
    <span class="string3">즐겁고 신나는 여행 더 효율적이고</span>
    <span class="string4">편하게 계획할 수 있습니다</span>

    	<div></div>
    	<span class="miniTitle">여행 짜조</span>
    	<!-- <img src="../image/main_image/MyPage_button.png"/> -->
        <input type="button" class="login" onclick="javascript:kakaoLogin();" id="Login_out" value="Log in"></input>
        
        <span class="developer">Developers: </span>
        <input type="button" class="goschedule" onclick="javascript:checkIsLogin();"></input>
	  
	    <script src="start.js"></script>
    </body>
</html>