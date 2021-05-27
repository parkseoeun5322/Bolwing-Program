<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#example2 { width: 570px; }
	#example2 thead tr { background-color: #ededed; }
</style>
</head>
<body>
	<c:choose>
		<c:when test="${fn:length(ranking) ne 0 || !empty ranking}">
		<%-- <c:when test="${ranking ne null }"> --%>
			<table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
				<thead>
					<tr>
						<th>순위</th>
						<th>이름</th>
						<th>총점</th>
						<th>게임번호</th>
						<th>대표 플레이어 외</th>
						<th>시작날짜</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ranking }" var="vo" varStatus="status">
						<c:if test="${status.count <= 5}">
							<tr>
								<td>${vo.rank }</td>
								<td>${vo.name }</td>
								<td>${vo.total_score }</td>
								<td>${vo.gseq }</td>
								<td>${vo.firstPlayer }외 ${vo.numberOfPlayer - 1 }명</td>
								<td><fmt:formatDate value="${vo.startDate }" pattern="yyyy년 MM월 dd일" /></td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<div style="height: 80px; line-height: 80px;">등록된 플레이어 정보가 존재하지 않습니다!</div>
		</c:otherwise>
	</c:choose>
</body>
</html>