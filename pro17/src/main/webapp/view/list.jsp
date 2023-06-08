<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<c:set var="list" value="${map.list}" />
	<c:set var="tot" value="${map.tot}" />
	<c:set var="section" value="${map.section}" />
	<c:set var="pageNum" value="${map.pageNum}" />

<%
	request.setCharacterEncoding("utf-8");
	response.setContentType("text/html; charset=UTF-8");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록창</title>
<style>
.no-uline {
	text-decoration: none;
}

.sel-page {
	text-decoration: none;
	color: red;
}

.cls1 {
	font-size: 20px;
	text-align: center;
}

.cls2 {
	font-size: 15px;
	text-align: center;
}
</style>
</head>
<body>
	<table border="1" align="center">
		<tr height="10" style="text-align: center; background-color: lightgreen;">
			<td>글번호</td>
			<td>작성자</td>
			<td>제목</td>
			<td>작성일</td>
		</tr>
		<c:choose>
			<c:when test="${empty list}">
				<tr>
					<td colspan="4">
						<p align="center">
							<b><span style="font-size: 9pt;"></span></b>
						</p>
					</td>
				</tr>
			</c:when>
			<c:when test="${!empty list}">
				<c:forEach var="item" items="${list}" varStatus="sts">
					<tr align="center">
						<td width="5%">${sts.count}</td>
						<td width="10%">${item.id}</td>
						<td align="left" width="35%">
							<span style="padding-right: 30px;"></span> 
							<c:choose>
								<c:when test="${item.level >1}">
									<c:forEach begin="1" end="${item.level}" step="1">
										<span style="padding-left: 20px"></span>
									</c:forEach>
									<span style="font-size: 12px;">[답변]</span>
									<a class="cls1" href="/board/view.do?articleNO=${item.articleNO}">${item.title}</a>
								</c:when>
								<c:otherwise>
									<a class="cls1" href="/board/view.do?articleNO=${item.articleNO}">${item.title}</a>
								</c:otherwise>
							</c:choose>
						</td>
						<td><fmt:formatDate value="${item.writeDate}" /></td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	<div class="cls2">
		<c:if test="${tot != null }">
			<c:choose>
				<c:when test="${tot > 100 }">
					<c:forEach var="page" begin="1" end="10" step="1">
						<c:if test="${section > 1 && page==1 }">
							<a class="no-uline" href="/board/list.do?section=${section-1}&pageNum=${(section-1)*10 +1 }">&nbsp; pre </a>
						</c:if>
						<a class="no-uline" href="/board/list.do?section=${section}&pageNum=${page}">${(section-1)*10 +page }
						</a>
						<c:if test="${page ==10 }">
							<a class="no-uline" href="/board/list.do?section=${section+1}&pageNum=${section*10+1}">&nbsp;next</a>
						</c:if>
					</c:forEach>
				</c:when>
				<c:when test="${tot ==100 }">
					<c:forEach var="page" begin="1" end="10" step="1">
						<a class="no-uline" href="#">${page} </a>
					</c:forEach>
				</c:when>

				<c:when test="${tot< 100 }">
					<c:forEach var="page" begin="1" end="${tot/10 +1}" step="1">
						<c:choose>
							<c:when test="${page==pageNum }">
								<a class="sel-page"
									href="/board/list.do?section=${section}&pageNum=${page}">${page }
								</a>
							</c:when>
							<c:otherwise>
								<a class="no-uline"
									href="/board/list.do?section=${section}&pageNum=${page}">${page }
								</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
	</div>
	<br>
	<br>
	<a class="cls1" href="/board/Form.do">
		<p class="cls2">글쓰기</p>
	</a>
</body>
</html>