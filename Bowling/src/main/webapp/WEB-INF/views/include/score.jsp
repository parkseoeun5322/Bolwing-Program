<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="bonus" value="${bonus }"></c:set>
<c:forEach items="${score }" var="s" varStatus="pstatus">
	<c:if test="${pstatus.index < 10 }">
		<c:choose>
			<c:when test="${pstatus.index eq 9 }">
				<td colspan="3" class="frame player${pseq } frame10">${s }</td>
			</c:when>
			<c:otherwise>
				<td colspan="2"
					class="frame player${pseq } frame${pstatus.index+1 }">${s }</td>
			</c:otherwise>
		</c:choose>
	</c:if>
</c:forEach>
<body>
	<script type="text/javascript">
		/*
		scoreBoard의 script는 score.jsp에 적용되지 않으므로 따로 추가
		*/

		var pseq = ${gvo.pseq}, frame = ${gvo.frame};
	
		printScore("player" + pseq, "frame" + frame);
		isBonus(pseq, frame);
		
		// 현재 플레이중인 플레이어의 프레임을 표시해주는 메소드
		function printScore(player, frame) {
			//alert("frame : " + frame + "player : " + player);
			$("." + frame).addClass("bg_blue");
			$(".frame").not("." + player).removeClass("bg_blue");
			$(".frame").not("." + frame).removeClass("bg_blue");
			return;
		}

		function isBonus(pseq, frame) {
			//alert("pseq = " + pseq + " / frame = " + frame + "bonus[0] = " + ${bonus[0]});
		}

	</script>
</body>