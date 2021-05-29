<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	a { color: #000; }
	.container {
		width: 1080px !important; 
		min-height: 500px !important; 
		margin: 0 auto !important; 
	}
	h3 { font-weight: 900; }
	a:link, a:visited { text-decoration: none; }
	
	.leftWrap { margin: 35px 0 0 0; }
	
	select {
	  padding: 6px 10px; 
	  font-size: 0.9rem;
	  font-family: inherit;
	  background-size: 15px 15px;
	  border: 1px solid #333;
	  background-color: #fff;
	  border-radius: 0;
	  outline: none;
	}

	select:focus {
	  border-color: #0094e1;
	}
	.scoreBoardWrap {
		border: 1px solid #ddd;
    	width: 450px;
    	border-radius: 10px;
   		padding: 20px;
	}
	#newGameForm > div { min-height: 100px; }
	
	.playerInfo { width: 300px; margin: 10px 0; }
	.input-group-addon:first-child {
		background-color: #ddd;
    	width: 35px;
	}
	svg.fa-user {
	    color: #757575;
    	font-size: 20px;
    	margin: 8px;
	}
	svg.fa-minus-square {
	    color: #bababa;
    	font-size: 26px;
    	margin: 5px 0 0 5px;
	}
	.gameListWrap {
		margin: 35px 0 0 20px;
	}
	.nav-tabs-custom { width: 450px; background-color: #fff; }
	.nav-tabs > li { width: 50%; height: 35px; line-height: 35px; text-align: center; font-weight: 900; }
	.nav-tabs > li > a { display: block; border: 1px solid #999999; }
	.nav-tabs > li > a.active { background-color: #999999; color: #fff; }
	.tab-pane { padding: 15px 20px; height: 385px; overflow-y: scroll; }
	.tab-pane > div { margin: 5px 0; }
	.tab-pane span {
	    padding: 6px;
    	border-radius: 4px;
    	color: #fff;
    	line-height: 40px;
    	margin: 0 5px 0 0;
	}
	.tab-content {
		border: 1px solid #999999;
		border-top: none;
	}
</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="leftWrap col-sm">
				<div class="">
					<h3>Ranking Top 5&nbsp;&nbsp;<a onclick="resetRank();"><i class="fas fa-sync-alt" style="font-size: 18px;"></i></a></h3>
					<div id="rankingList"></div>
				</div>
				<div class="scoreBoardWrap">
					<h3>Score Board</h3>
					<form action="newGame" method="post" id="newGameForm">
						<div>
							<select id="numberOfPlayer" name="numberOfPlayer"
								onchange="selectPlayer()">
								<option selected="selected" value="0" disabled>플레이어 수</option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
							</select>
							<div id="inputPlayer">
								
							</div>
						</div>
						<a onclick="newGame();" class="btn btn-block btn-danger btn-lg" style="width: 150px; font-weight: 900;">Game Start!</a>
					</form>
				</div> <!-- .scoreBoardWrap -->
			</div> <!-- .col -->
			<div class="gameListWrap col-sm">
				<h3>Game List</h3>
				<p>저장된 게임을 불러올 수 있습니다.</p>
				<c:choose>
					<c:when test="${fn:length(game) ne 0 || !empty game}">
						<div>
							<div class="nav-tabs-custom">
								<ul class="nav nav-tabs">
									<li><a href="#tab_1" data-toggle="tab" aria-expanded="true" class="active">진행 중</a></li>
									<li><a href="#tab_2" data-toggle="tab" aria-expanded="false">게임 종료</a></li>
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tab_1">
										<c:choose>
											<c:when test="${fn:length(proceeding) ne 0 || !empty proceeding}">
												<c:forEach items="${proceeding }" var="vo" varStatus="status">
													<div>
														<span style="background-color: #119665;"><fmt:formatDate value="${vo.startDate }" pattern="yyyy년 MM월 dd일" /></span>
														<a href="loadGame?gseq=${vo.gseq}">
															<b>${vo.firstPlayer }</b>
															<c:if test="${vo.numberOfPlayer ne 1 }">외 ${vo.numberOfPlayer - 1 } 명</c:if>
														</a>
													</div>
												</c:forEach>
											</c:when>
											<c:otherwise>진행중인 게임이 존재하지 않습니다.</c:otherwise>
										</c:choose>
									</div>
									<div class="tab-pane" id="tab_2">
										<c:choose>
											<c:when test="${fn:length(gameover) ne 0 || !empty gameover}">
												<c:forEach items="${gameover }" var="vo" varStatus="status">
													<div>
														<span style="background-color: #707070;"><fmt:formatDate value="${vo.startDate }" pattern="yyyy년 MM월 dd일" /></span>
														<a href="loadGame?gseq=${vo.gseq}">
															<b>${vo.firstPlayer }</b>
															<c:if test="${vo.numberOfPlayer ne 1 }">외 ${vo.numberOfPlayer - 1 } 명</c:if>
														</a>
													</div>
												</c:forEach>
											</c:when>
											<c:otherwise>종료한 게임이 존재하지 않습니다.</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise><div>등록된 게임 리스트가 존재하지 않습니다!</div></c:otherwise>
				</c:choose>
			</div>
		</div> <!-- .row -->
	</div> <!-- .container -->
	<script type="text/javascript">
		resetRank();

		//랭킹 관리 새로 고침시 실행되는 메소드 
		function resetRank() {
			$.ajax({
				url : "resetRank",
				data : {},
				success : function(data) {
					$("#rankingList").html(data);
				},
				error : function(req, text) {
					alert(req + " : " + text);
				}
			});
		} //resetRank()

		//플레이어 수 추가 시 input 요소 추가 메소드
		function selectPlayer() {
			var player = $(".playerInfo").length;
			//var numberOfPlayer = $("#numberOfPlayer");
			var selectValue = $("#numberOfPlayer option:selected").val();

			if (player > selectValue) {
				for (var i = player - selectValue; i > 0; i--) {
					$(".playerInfo").last().remove();
				}
			} else {
				for (var i = 1; i <= selectValue - player; i++) {
					var txt = "<div class='input-group playerInfo'>";
                	txt += "<span class='input-group-addon'><i class='fas fa-user'></i></span>";
                	txt += "<input type='text' class='form-control playerName' onfocus='this.placeholder=\"\"'>";
                	txt += "<span class='input-group-addon'><i class='fas fa-minus-square'></i></span>";
              		txt += "</div>";
					$("#inputPlayer").append(txt);
				}
			}

			// 플레이어를 선택할 때마다 요소에 따라 name 재설정
			$(".playerName").each(
				function(index) {
					if (index == 0) {
						$(this).attr({
							"name" : "firstPlayer",
							"placeholder" : "대표 플레이어 이름"
						}).next().attr("onclick",
								"removePlayer(" + (index + 1) + ")");
					} else {
						$(this).attr({
							"name" : "player" + (index + 1),
							"placeholder" : "플레이어 이름"
						}).next().attr("onclick",
							"removePlayer(" + (index + 1) + ")");
					}
				});
		} //selectPlayer()

		function newGame() {
			var numberOfPlayer = document.getElementById("numberOfPlayer");
			var selectValue = numberOfPlayer.options[numberOfPlayer.selectedIndex].value;
			if (selectValue == 0) {
				alert("플레이어 수를 선택해주세요.");
			} else {
				$(".playerName").each(function(index) {
					if ($(this).val() == "") {
						alert("플레이어 이름을 입력해주세요.");
						return false;
					} else {
						if ((index + 1) == selectValue)
							$("#newGameForm").submit();
						// 마지막 요소까지 플레이어 이름이 입력된 후에 submit을 시켜준다.
					}
				});
			}
		} //newGame()

		function removePlayer(playerNum) {
			if (playerNum == 1) {
				alert("대표 플레이어는 삭제하실 수 없습니다.");
			} else {
				$("input[name=player" + playerNum + "]").parent("div").remove();
				$("#numberOfPlayer").val($(".playerInfo").length).prop("selected", true);
				$(".playerName").each(function(index) {
					$(this).attr("name", "player" + (index + 1)).next()
					.attr("onclick", "removePlayer(" + (index + 1) + ")");
				});
			}

		} //removePlayer()
	</script>
</body>
</html>