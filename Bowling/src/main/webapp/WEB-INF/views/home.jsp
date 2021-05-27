<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Home</title>
	<style type="text/css">
		.scoreTable { width: 200px; }
	</style>
</head>
<body>
	<div class="wrapper">
		<div class="scoreBoard">
			<form action="play">
				<span>플레이어 수를 입력하세요 (최대 5인)</span>
				<input type="number" name="playNum" min="1" max="5">
				<input type="submit" value="Game Start!">
			</form>
			<form action="roll" id="rollForm">
				
				<input type="button" value="1" onclick="roll_go(1);">
				<input type="button" value="2" onclick="roll_go(2);">
				<input type="button" value="3" onclick="roll_go(3);">
				<input type="button" value="4" onclick="roll_go(4);">
				<input type="button" value="5" onclick="roll_go(5);">
				<input type="button" value="6" onclick="roll_go(6);">
				<input type="button" value="7" onclick="roll_go(7);">
				<input type="button" value="8" onclick="roll_go(8);">
				<input type="button" value="9" onclick="roll_go(9);">
				<input type="button" value="10" onclick="roll_go(10);">
				
				<input type="hidden" name="roll" value="" id="rollNum">
				<input type="number" name="playNum" value="${playNum }">
			</form>
			<div class="score">
				<c:forEach items="${dto2 }" var="dto">
					<table class="scoreTable" border="1">
						<tr>
							<c:forEach var="i" begin="0" end="20" step="1">
								<td>${dto.score_list[i] }</td>
							</c:forEach>
						</tr>
						<tr>
							<c:forEach var="i" begin="0" end="9" step="1">
								<td colspan="2">${dto.total_list[i] }</td>
							</c:forEach>					
						</tr>
					</table>
				</c:forEach>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		function roll_go(num) {
			document.getElementById('rollNum').value = num;
			document.getElementById('rollForm').submit();
		}
	</script>
</body>
</html>
