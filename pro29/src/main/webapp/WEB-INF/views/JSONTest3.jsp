<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html>
<head>
<title>JSONTest3</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>  
<script>
  $(function() {
      $("#checkJson").click(function() {
      	var article = {id:"kmk", 
      			name:"김민경",
      			pwd:"1234", 
      			email:"kmk@test.com"
	              };
  
  	$.ajax({
  	    type:"POST",
        url:"${contextPath}/boards",
        //type:"PUT",
        //url:"${contextPath}/boards/918",
       // type:"DELETE",
        //url:"${contextPath}/boards/918",
        contentType: "application/json",
        data :JSON.stringify(member),
      success:function (data,textStatus){
          alert(data);
      },
      error:function(data,textStatus){
        alert("에러가 발생했습니다.");ㅣ
      },
      complete:function(data,textStatus){
      }
   });  //end ajax	

   });
});
</script>
</head>
<body>
  <input type="button" id="checkJson" value="새글 쓰기"/><br><br>
  <div id="output"></div>
</body>
</html>