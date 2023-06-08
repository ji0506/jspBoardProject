<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*,pro17.se00.*"
    %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<%
	request.setCharacterEncoding("utf-8");
	response.setContentType("text/html; charset=UTF-8");
%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	.cls1{
		font-size: 40px;
		text-align: center;
	}
	.cls2{
		font-size: 20px;
		text-align: center;
	}
</style>
	<c:choose>
		<c:when test="${msg == 'add'}">
			<script type="text/javascript">
				window.onload = function(){
					alert("회원을 등록했습니다.");
				}
			</script>
		</c:when>
		<c:when test="${msg == 'mod'}">
			<script type="text/javascript">
				window.onload = function(){
					alert("회원 정보를 수정했습니다.");
				}
			</script>
		</c:when>
		<c:when test="${msg == 'del'}">
			<script type="text/javascript">
				window.onload = function(){
					alert("회원 정보를 삭제했습니다.");
				}
			</script>
		</c:when>
	</c:choose>
</head>
<body>
	<p class="cls1">회원정보</p>
    <table border="1" align="center">
		<tr style="text-align: center; background-color: lightgreen;">
			<td style="width: 7%;"><b>아이디</b></td>
			<td style="width: 7%;"><b>비밀번호</b></td>
			<td style="width: 7%;"><b>이름</b></td>
			<td style="width: 7%;"><b>이메일</b></td>
			<td style="width: 7%;"><b>가입일</b></td>
			<td style="width: 7%;"><b>수정</b></td>
			<td style="width: 7%;"><b>삭제</b></td>
		</tr>
		<c:choose>
			<c:when test="${empty memList}">
				<tr>
					<td colspan=5><b>등록된 회원이 없습니다.</b></td>
				</tr>
			</c:when>
			<c:when test="${!empty memList }">
				<c:forEach var="mem" items="${memList}">
					<tr style="text-align: center;">
						<td>${mem.id}</td>
						<td>${mem.pwd}</td>
						<td>${mem.name}</td>
						<td>${mem.email}</td>
						<td>${mem.joinData}</td>
						<td><a href="/member/modForm.do?id=${mem.id}">수정</a></td>
						<td><a href="/member/del.do?id=${mem.id}">삭제</a></td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
    </table>
	<a href="${contextPath}/member/memberForm.do">
		<p class="cls2">회원정보</p>
	</a>
</body>
</html>