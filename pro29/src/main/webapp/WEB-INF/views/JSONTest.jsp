<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html>
<head>
<title>JSONTest</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>  
<script>
//클라이언트  -> 서버로 데이터를 json 문자열 형태로 전달
  $(function() {
      $("#checkJson").click(function() { // 아이디 #checkJson 클릭이벤트
    	  // 자바 버전으로 map과 비슷한 구조
      	var member = { id:"park", 
  			    name:"박지성",
  			    pwd:"1234", 
  			    email:"park@test.com" };
      
      // 서버에 데이터를 비동기형식으로 전달
  	$.ajax({
        type:"post",        
        url:"${contextPath}/test/info", // 폼 태그 action과 비슷한 역할, 서버에게 전달하는 주소
        contentType: "application/json",
        data :JSON.stringify(member), //자바스크립트 객체형태로 작성은 했지만 전달은 문자열로 바뀌어서 전달
     success:function (data,textStatus){
     },
     error:function(data,textStatus){
        alert("에러가 발생했습니다.");
     },
     complete:function(data,textStatus){
     }
  });  //end ajax	

   });
});
</script>
</head>
<body>
  <input type="button" id="checkJson" value="회원 정보 보내기"/><br><br>
  <div id="output"></div>
</body>
</html>