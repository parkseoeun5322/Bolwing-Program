<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	.scoreBoard { width: 500px; }
	.scoreBoard th, .scoreBoard td { height: 20px; }
	.bg_transparent { background-color: transparent; }
	.bg_blue { background-color: #e0ecff !important; }
	.isStrike1 { 
		background: url("img/strike1.png") center /15px no-repeat;
		width: 19px;
		color: rgba(0,0,0,0);
	 }
	 .isStrike1 + td { 
		width: 19px;
		background: url("img/strike2.png") center /15px no-repeat;
	 }
	.isStrike { 
		background: url("img/strike.png") center /30px no-repeat;
		color: rgba(0,0,0,0);
	 }
	 .isSpare {
		background: url("img/spare.png") center /30px no-repeat;
		color: rgba(0,0,0,0);
	 }

</style>
</head>
<body>
	<div id="scoreBoard">
		<h2>Score Board</h2>
		<p>플레이어 수는 최대 4명입니다.</p>
		<c:forEach items="${list }" var="vo" varStatus="status">
			<c:set var="pseq" value="${vo.pseq }"></c:set>
			<h3>${vo.name }</h3>
			<h4 class="event event${vo.pseq }">
				<c:if test="${vo.total_score ne null}">
					<c:if test="${vo.total_score >= 200 }">Perfect!</c:if>
					<c:if test="${vo.total_score >= 100 && vo.total_score < 200 }">Good!</c:if>
					<c:if test="${vo.total_score < 100 }">Game Over!</c:if>
				</c:if>
				<c:if test="${vo.total_score eq null}">
					<c:if test="${gvo.pseq eq vo.pseq }">Playing!</c:if>		
					<c:if test="${gvo.pseq ne vo.pseq }">Waiting...</c:if>		
				</c:if>
			</h4>
			<table class="scoreBoard" border="1">
				<tr>
					<c:forEach var="i" begin="1" end="10">
						<c:if test="${i eq 10 }"><th colspan="3" class="frame player${vo.pseq } frame10">10</th></c:if>
						<c:if test="${i ne 10 }"><th colspan="2" class="frame player${vo.pseq } frame${i }">${i }</th></c:if>
					</c:forEach>
					<td><b>Total</b></td>
				</tr>
				<tr class="pins pins${vo.pseq}">
					<%--
					<c:forEach var="j" begin="1" end="10" varStatus="status2">
						<td>${vo[ball_j_1]}</td>
						<td>${vo[ball_j_2]}</td>
					</c:forEach>
					 --%>
					<c:forEach items="${printMap }" var="printMap">
						<c:if test="${printMap.get('pseq') eq vo.pseq}">
							<c:set var="bonus" value="${printMap.get('bonusList') }"></c:set>
						</c:if>
					</c:forEach>
					<td class="frame player${vo.pseq } frame1 ${bonus[0]}">${vo.ball_1_1 }</td>
					<td class="frame player${vo.pseq } frame1 ${bonus[1] }">${vo.ball_1_2}</td>
					<td class="frame player${vo.pseq } frame2 ${bonus[2] }">${vo.ball_2_1}</td>
					<td class="frame player${vo.pseq } frame2 ${bonus[3] }">${vo.ball_2_2}</td>
					<td class="frame player${vo.pseq } frame3 ${bonus[4] }">${vo.ball_3_1}</td>
					<td class="frame player${vo.pseq } frame3 ${bonus[5] }">${vo.ball_3_2}</td>
					<td class="frame player${vo.pseq } frame4 ${bonus[6] }">${vo.ball_4_1}</td>
					<td class="frame player${vo.pseq } frame4 ${bonus[7] }">${vo.ball_4_2}</td>
					<td class="frame player${vo.pseq } frame5 ${bonus[8] }">${vo.ball_5_1}</td>
					<td class="frame player${vo.pseq } frame5 ${bonus[9] }">${vo.ball_5_2}</td>
					<td class="frame player${vo.pseq } frame6 ${bonus[10] }">${vo.ball_6_1}</td>
					<td class="frame player${vo.pseq } frame6 ${bonus[11] }">${vo.ball_6_2}</td>
					<td class="frame player${vo.pseq } frame7 ${bonus[12] }">${vo.ball_7_1}</td>
					<td class="frame player${vo.pseq } frame7 ${bonus[13] }">${vo.ball_7_2}</td>
					<td class="frame player${vo.pseq } frame8 ${bonus[14] }">${vo.ball_8_1}</td>
					<td class="frame player${vo.pseq } frame8 ${bonus[15] }">${vo.ball_8_2}</td>
					<td class="frame player${vo.pseq } frame9 ${bonus[16] }">${vo.ball_9_1}</td>
					<td class="frame player${vo.pseq } frame9 ${bonus[17] }">${vo.ball_9_2}</td>
					<td class="frame player${vo.pseq } frame10 ${bonus[18] }">${vo.ball_10_1 }</td>
					<td class="frame player${vo.pseq } frame10 ${bonus[19] }">${vo.ball_10_2 }</td>
					<td class="frame player${vo.pseq } frame10 ${bonus[20] }">${vo.ball_10_3 }</td>
					<td rowspan="3" class="totalScore total${vo.pseq }">
						<c:choose>
							<c:when test="${vo.total_score ne null }">${vo.total_score}</c:when>
							<c:otherwise>
								<c:forEach items="${printMap }" var="printMap">
									<c:if test="${printMap.get('pseq') eq vo.pseq}">${printMap.get('totalScore') }</c:if>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr id="scores${vo.pseq }">
					<c:forEach items="${printMap }" var="printMap">
						<c:if test="${printMap.get('pseq') eq vo.pseq}">
							<c:forEach items="${printMap.get('scoreList') }" var="score" varStatus="stt">
								<c:choose>
									<c:when test="${stt.index eq 9 }">
										<td colspan="3" class="frame player${vo.pseq } frame10">${score }</td>
									</c:when>
									<c:otherwise>
										<td colspan="2" class="frame player${vo.pseq } frame${stt.index+1 }">${score }</td>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
					</c:forEach>
				</tr>
			</table>
		</c:forEach>
	</div>
	<div>
		<div class="proceeding y">
				<div id="playerNameWrap"><b id="playerName"></b><span>차례입니다!</span></div>
				<div id="rollBtn">
						
				</div>
				<a onclick="delete_roll()" class="deleteBtn">delete</a>
				<div>
					<a onclick="selfTest(1, ${gvo.gseq})">All Spare</a>
					<a onclick="selfTest(2, ${gvo.gseq })">Perfect</a>
					<a onclick="selfTest(3, ${gvo.gseq })">All Random</a>
				</div>
				<br>
				<a onclick="reset();">Reset</a>	
		</div>
		<div class="proceeding n">
				<div>게임이 종료되었습니다!</div>
				<a onclick="delete_roll()" class="deleteBtn">delete</a>
				<br/>
				<a onclick="reset();">Reset</a>
		</div>
	</div>
	<script type="text/javascript">
		/* 수정 모드로 바꿈 > 핀 삭제 > 핀 입력 > 핀 삭제시 오류 
			update = true > 삭제할 때마다 frame, turn, pseq 변수에 값 초기화 
			> 입력할 때마다 updateFrame, updateTurn, updatePseq 변수에 값 초기화(update가 true이므로)
			> 입력이 되어있는 플레이어의핀 점수를 삭제하는 것이 아니라 그 전 플레이어의 핀점수를 삭제하게 됨
		*/
	
		var frame = ${gvo.frame}, turn = ${gvo.turn}, pseq = ${gvo.pseq};
		var name = "${gvo.playerName}", remainPin = ${gvo.remainPin}, progress = "${gvo.proceeding}";
		var updateFrame, updateTurn, updatePseq, updateRemainPin = 10;
		var update = false;		//수정 조건(핀 입력이 완료된 프레임만) 충족 시 true / 수정 조건 충족 x or 수정 취소를 눌렀을 경우 false
		var updated = false;	//수정 모드로 이미 핀을 하나 이상 입력한 경우 true / 수정을 완료한 경우 false

		//초기 화면 셋팅
		proceeding("${gvo.proceeding}", ${gvo.pseq}, ${gvo.frame}, ${gvo.remainPin}, "${gvo.playerName}");

		//게임 종료 여부 판별 > 스코어보드 우측 상단 알림판 출력(핀버튼, 플레이어 이름/수정중, 배경 하이라이트)
		function proceeding(proceeding, pseq, frame, remainPin, name) {
			if(proceeding == "Y") {
				var ele = "";
				//종료 여부 판별
				$(".proceeding.y").css("display", "block");
				$(".proceeding.n").css("display", "none");

				//핀 버튼 셋팅
				for(var i = 0; i <= remainPin; i++) {
					ele += "<input type='button' value='" + i + "' onclick='roll_go(" + i + ");'>";
				}
				$("#rollBtn").html(ele);

				// 플레이어 이름 및 수정중 표시
				if(name == "") {	//플레이어 이름이 없을 경우에는 수정모드로 간주
					$("#playerNameWrap").html("<b id='playerName'>수정중입니다.</b><a id='resetUpdateBtn' onclick='resetUpdate()'>수정 취소</a>");
					if(updated) $("#resetUpdateBtn").css("display", "none");
					// 수정모드일 때 이미 한개 이상의 핀이 입력된 경우에는 수정 취소할 수 없게끔 함
				} else {
					$("#playerNameWrap").html("<b id='playerName'>" + name + "</b><span>차례입니다!</span>");
				}

				// 해당 차례의 프레임 배경색 표시
				$(".frame" + frame).addClass("bg_blue");
				$(".frame").not(".player" + pseq).removeClass("bg_blue");
				$(".frame").not(".frame" + frame).removeClass("bg_blue");
			} else {
				$(".proceeding.n").css("display", "block");
				$(".proceeding.y").css("display", "none");
				
				if($(".frame").hasClass("bg_blue")) $(".frame").removeClass("bg_blue");
				// 게임 종료 시 혹은 셀프 테스트시 첫번째 플레이어의 첫번째 프레임의 배경색을 없애줌
			}
		}

		//점수판 우측 상단에 이벤트 알림 출력
		function eventPrint(type, data, prevPseq, nextPseq, bonus) {
			//입력, 삭제, 셀프테스트, 리셋, 수정중, 수정완료
			if(type == "reset") {
				$(".event").text("");
			} else if(type == "delete") {
				if(data.frame == 10) {
					$(".event" + prevPseq).text("Playing!");
					$(".event").each(function(index) {
						if(prevPseq <= index) $(this).text("Waiting!");
						// 10프레임 삭제 시 삭제를 하지 않은 플레이어는 text를 바꾸지 않게 하기 위해
					});
				} else {
					$(".event" + prevPseq).text("Playing!");
					$(".event").not(".event" + prevPseq).text("Waiting!");
				}
			} else {
				if(data.totalScore != null) {	//플레이어별 게임 완료
					if(data.totalScore >= 200) {
						$(".event" + prevPseq).text("Perfect!");
					} else if(data.totalScore >= 100) {
						$(".event" + prevPseq).text("Good!");
					} else {
						$(".event" + prevPseq).text("Game Over!");
					}
				} else {	//플레이어별 게임 완료 x
					if(bonus == "isStrike" || bonus == "isStrike1") {
						$(".event" + prevPseq).text("Strike!");
					} else if(bonus == "isSpare") {
						$(".event" + prevPseq).text("Spare!");
					} else {
						$(".event").not(".event" + nextPseq).text("Waiting!");
					}
					
					$(".event" + nextPseq).text("Playing!");
				}
			}
			//1. 게임 끝남 > gameover / Perfect! / Good!
			//2. 게임 안끝남 > strike, spare, waiting, playing

			// 자기 순서 x > waiting
			// 자기 순서 o > strike, spare, playing
		}

		//보너스 처리(선택자를 통해 배경 이미지 출력)
		function isBonus(bonus, $node) {
			if(bonus != null) {
				if(bonus == "") {
					$node.removeClass("isStrike1 isStrike isSpare");
				} else {
					$node.addClass(bonus);
				}
			} else {
				$node.removeClass("isStrike1 isStrike isSpare");
				/*
				if(pseq == null) {	//전체 초기화 (리셋 / selftTest 초기화)
					$(".event").text("");	
				} else {		//해당프레임만 초기화(삭제, 수정중)
					$(".event" + pseq).text("");
				}
				*/
			}
		}

		// 핀 점수 출력 및 삭제 및 초기화(핀 입력 또는 삭제시 사용)
		function setScore(type, frame, prevPseq, nextPseq, turn, map, num) {
			var $pin, $scores;
			var data = map.gvo;
			var bonus = map.bonusList;
			if(type == "reset") {			//전체 리셋할때
				data = map;					//리셋할 때는 map이 아닌 gvo를 받아오므로 data변수를 초기화시킴
				isBonus(null, $(".frame"));
				eventPrint("reset");
				//$(".event").text("");
			} else {
				if(turn == 1) {
					$pin = $(".pins" + prevPseq).children(".frame" + frame).eq(0);
					if(type == "roll") {
						isBonus(bonus[frame * 2 - 2], $pin);
						eventPrint("roll", data, prevPseq, nextPseq, bonus[frame * 2 -2]);
					}
				} else if(turn == 2) {
					$pin = $(".pins" + prevPseq).children(".frame" + frame).eq(1);
					if(type == "roll") {
						isBonus(bonus[frame * 2 - 1], $pin);
						eventPrint("roll", data, prevPseq, nextPseq, bonus[frame * 2 -1]);
					}
				} else {
					$pin = $(".pins" + prevPseq).children(".frame" + frame).eq(2);
					if(type == "roll") {
						isBonus(bonus[20], $pin);
						eventPrint("roll", data, prevPseq, nextPseq, bonus[20]);
					}
				}
				
				if(type == "roll") {		//핀을 굴릴때
					$pin.text(num);
				} else if (type == "delete") {	//핀을 삭제할 때
					$pin.text("");
					isBonus(null, $pin);
					eventPrint("delete", data, prevPseq, nextPseq, "");
					//$pin.removeClass("isStrike");
					//$pin.removeClass("isSpare");
				}
				
				//$("#total" + pseq).text(data.totalScore);
			}

			//console.log("setScore() totalScore = " + data.totalScore);
		} //setScore()
		
		// 각 프레임당 총점 부분 / 플레이어 당 최종 총점 출력
		function scoreBoard(type, pseq, score, totalScore) {
			if(type == "reset") {		//리셋할때
				$("td.frame").text("");		
				$(".totalScore").text("");
			} else if(type == "updating") {		//수정중일 때
				$("#scores" + pseq).children("td").empty();
				$(".total" + pseq).empty();
			} else {	//그 외(roll, selfTest, delete)
				var $scores = $("#scores" + pseq).children("td");
				for(var i = 0; i < 10; i++) {
					$scores.eq(i).text(score[i]);
				}
				$(".total" + pseq).text(totalScore);
			}
		}
		
		//ajax에 쓰일 공통 코드(roll / delete / reset)
		function setGameInfo(type, frame1, pseq1, turn1, map, num) {
			var data = map;
			
			if(type != "reset") {	//reset일 때 받아오는 정보가 map이 아닌 vo이다.
				data = map.gvo;
			}
			
			setScore(type, frame1, pseq1, data.pseq, turn1, map, num);
			scoreBoard(type, pseq1, map.scoreList, map.totalScore);
			frame = data.frame;
			pseq = data.pseq;
			turn = data.turn;
			name = data.playerName;
			remainPin = data.remainPin;
			progress = data.proceeding;
			proceeding(progress, pseq, frame, remainPin, name);
		}
		
		// 핀을 굴릴때 실행
		function roll_go(num) {
			// 수정할 프레임, 플레이어 번호, 투구번호, 핀 갯수를 보내기 위한 게임 정보 관련 데이터
			var param = { 
				gseq: ${gvo.gseq}, 
				pseq: updatePseq, 
				frame: updateFrame, 
				turn: updateTurn, 
				roll: num,
				remainPin: updateRemainPin,
				numberOfPlayer: ${gvo.numberOfPlayer}
			}
			
			if(update) { 	//핀 점수 수정시
				if(updateTurn == 1) {
					$(".pins" + updatePseq).children(".player" + updatePseq + ".frame" + updateFrame).empty();
					// 해당 프레임의 입력되어 있는 핀 점수 초기화
				}
				
				$.ajax({
					type: "POST",
					url: "updateRoll",
					data : JSON.stringify(param),
					dataType : 'json',
					contentType : "application/json",
					success: function(map) {
						var data = map.gvo;
						setScore("roll", updateFrame, updatePseq, pseq, updateTurn, map, num);
						if( (${gvo.numberOfPlayer} == 1 && updateFrame != data.frame) ||
							(${gvo.numberOfPlayer} != 1 && updatePseq != data.pseq) ) {
							// 핀 수정이 끝났을 경우
							//update = false; updated = false;
							scoreBoard("updated", updatePseq, map.scoreList, map.totalScore);
							//proceeding(data.proceeding, pseq, frame, remainPin, data.playerName);
							resetUpdate();
							//$(".deleteBtn").css("display", "inline");
						} else {	//핀 수정이 진행중인 경우
							var selector = ".player" + updatePseq + ".frame" + updateFrame;
							scoreBoard("updating", updatePseq, map.scoreList, map.totalScore);
							// 수정 중일때는 각 프레임당 총점 부분과 최종 총점 부분을 비운다
							//$("#scores" + updatePseq).children("td").empty();
							//$(".total" + updatePseq).empty();
							isBonus(null, $("td" + selector).eq(updateTurn-1).nextAll(selector));
							//$("td" + selector).eq(updateTurn-1).nextAll(selector).removeClass("isStrike isSpare");
							updated = true;
							proceeding("Y", updatePseq, updateFrame, data.remainPin, "");
						}
						updatePseq = data.pseq;
						updateFrame = data.frame;
						updateTurn = data.turn;
						updateRemainPin = data.remainPin;
					}, error: function(req, text) {
						alert(req + " : " + text);
					}
				});
			} else {		//핀 점수 입력시
				$.ajax({
					url: "roll",
					data: { gseq: ${gvo.gseq}, rollNum: num },
					success: function(map) {
						setGameInfo("roll", frame, pseq, turn, map, num);
					}, error: function(req, text) {
						alert(req + " : " + text);
					}
				});
			}
		} //roll_go()
		
		// 셀프 테스트 게임
		function selfTest(selector, gseq) {
			$.ajax({
				url: "selfTest",
				data: {selector: selector, gseq: gseq},
				success: function(mapList) {
					isBonus(null, $(".frame"));
					//$(".frame").removeClass("isStrike isSpare");	//보너스 선택자 초기화
					$(".scoreBoard").each(function(index) {
						var map = mapList[index];
						for(var i = 0; i < 21; i++) {
							// selfTest 계산된 gvo를 가져오지 않기 때문에 setScore()를 쓸 수 X 
							var $pin = $(this).find(".pins" + map.pseq).children("td").eq(i);
							$pin.text(map.pinList[i]);
							isBonus(map.bonusList[i], $pin);
							//if(map.bonusList[i] == "isStrike") $pin.addClass(map.bonusList[i]);
							//else if(map.bonusList[i] == "isSpare") $pin.addClass(map.bonusList[i]);
						}
						frame = 11; turn = 1; pseq = 1;
						remainPin = 10, progress = "N";
						resetUpdate();	//수정 관련 변수 초기화
						scoreBoard("selfTest", map.pseq, map.scoreList, map.totalScore);
						eventPrint("selfTest", map, map.pseq);
						//$("#total" + map.pseq).text(map.totalScore);
					});
				}, error: function(req, text) {
					alert("테스트가 불가능합니다. 관리자에게 문의하세요.")
				}
			});
		} //selfTest()

		// 핀 삭제 시 실행
		function delete_roll() {
			if(pseq == null) pseq = ${gvo.numberOfPlayer};
			$.ajax({
				url: "delete",
				data: { gseq: ${gvo.gseq}, pseq: pseq },
				success: function(map) {
					var gvo = map.gvo;
					if(gvo == null) alert("삭제할 핀이 존재하지 않습니다!");
					else setGameInfo("delete", gvo.frame, gvo.pseq, gvo.turn, map);
				}, error: function(req, text) {
					alert("삭제하실 수 없습니다. 관리자에게 문의하세요.");
				}
			});
		} //delete_roll()

		// 전체 플레이어 점수 리스트 초기화
		function reset() {
			$.ajax({
				url: "reset",
				data: { gseq: ${gvo.gseq}, pseq: pseq },
				success: function(data) {
					setGameInfo("reset", data.frame, data.pseq, data.turn, data);
					resetUpdate();		// 수정조건 관련 초기화
				}, error: function(req, text) {
					alert(req + " : " + text);
				}
			});

			
		} //reset()

		// 프레임을 클릭했을 때 실행되는 이벤트 핸들러(수정모드 전환)
		$(".frame").click(function() {
			if(updated) {
				alert("수정 중에는 다른 프레임으로 변경할 수 없습니다.");
				return false;
			}
			var className = this.className.split(" ");
			updateTurn = 1;
			updatePseq = className[1].charAt(className[1].length - 1);
			updateFrame = className[2].charAt(className[2].length - 1);
			if(updateFrame == "0") updateFrame = 1 + updateFrame;
			// → 끝문자만 추출하므로 0일 경우 10이 되게끔 변수 초기화
			
			var $pins = $(".pins" + updatePseq).children("." + className[1] + "." + className[2]);
			var strike = false;
	
			$pins.each(function(index) {
				if(index != 2) {	//1투구 or 2투구
					if(index == 0 && $(this).text() == "10" && updateFrame != 10) {
					// 10프레임을 제외한 프레임이 스트라이크일 경우
						strike = true;
					}
					if($(this).text() == "") {
						if(!strike) {	//스트라이크일 경우 두번째 투구가 비어있으므로 그 경우는 제외
							alert("점수 입력이 완료된 프레임만 수정이 가능합니다.");
							update = false;		//수정 조건 불충족
							return false;
						}
					} else {	//수정 조건 충족
						//printTurn(className[1], className[2]);
						//setRollBtn(10);
						update = true;
					}
				} else {	//3투구
					if(turn == 3 && $(this).text() == "") {		//보너스 투구 미입력시
						alert("점수 입력이 완료된 프레임만 수정이 가능합니다.");
						update = false;
					}
				}
			});
			
			if(update) {	//수정 조건이 충족된 경우
				//printTurn(className[1], className[2]);
				//setRollBtn(10);
				proceeding("Y", updatePseq, updateFrame, 10, "");	//클릭 시 핀 버튼 부분 출력
				//setPlayerName("");	//클릭 시 진행중인 플레이어의 이름을 출력하지 않도록 함
				$(".deleteBtn").css("display", "none");
			}
 			//alert("pseq : " + updatePseq + " / frame : " + updateFrame + " / turn : " + updateTurn);

		});

		//수정 취소를 눌렀을 경우 메소드
		function resetUpdate() {
			update = false;
			updated = false;
			updateTurn = null;
			updatePseq = null;
			updateFrame = null;
			updateRemainPin = 10;
			proceeding(progress, pseq, frame, remainPin, name);
			//setRollBtn(remainPin);
			//setPlayerName(name);
			//printTurn(pseq, frame);
			$(".deleteBtn").css("display", "inline");
		} //resetUpdate()
	</script>
</body>
</html>