package com.comin.bowling.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comin.bowling.calculator.GameInfo;
import com.comin.bowling.calculator.PrintScoreBoard;
import com.comin.bowling.service.GameServiceImpl;
import com.comin.bowling.service.PlayerServiceImpl;
import com.comin.bowling.vo.GameVO;
import com.comin.bowling.vo.PlayerVO;

@Controller
public class GameController {
	@Autowired GameServiceImpl gService;
	@Autowired PlayerServiceImpl pService;
	GameInfo game_info = new GameInfo();
	GameVO gvo = null;
	List<PlayerVO> list = null;
	ArrayList<GameVO> updateGvo = new ArrayList<GameVO>();
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(Model model) {
		List<GameVO> game_list = null;							//모든 게임 정보 리스트
		ArrayList<GameVO> proceeding = new ArrayList<GameVO>();	//진행중인 게임 정보 리스트
		ArrayList<GameVO> gameover = new ArrayList<GameVO>();	//종료한 게임 정보 리스트
		
		try {
			game_list = gService.game_list();	//DB에 저장된 모든 게임 정보를 조회 
		} catch (Exception e) {
			System.out.println("main() DB Connect Error!");
		}
		
		for (GameVO gameVO : game_list) {
			if(gameVO.getProceeding().equals("Y")) {
				proceeding.add(gameVO);		//진행중인 게임 정보를 리스트에 추가
			} else {
				gameover.add(gameVO);		//종료한 게임 정보를 리스트에 추가
			}
		}
		
		model.addAttribute("game", game_list);
		model.addAttribute("proceeding", proceeding);
		model.addAttribute("gameover", gameover);
		
		return "main";			
	}
	
	// 게임 생성 메소드 (게임 정보 DB Insert / 플레이어 정보 DB Insert)
	@RequestMapping("/newGame")
	public String newGame(GameVO vo, HttpServletRequest request) {
		PlayerVO pvo = new PlayerVO();
		int gseq = 0;
		
		try {
			gseq = gService.game_insert(vo);	
			// → 게임 정보 DB Insert & 해당 게임 시퀀스 반환
		} catch (Exception e) {
			System.out.println("newGame() GAME DB Insert Error!");
		}
		
		// PlayerVO 셋팅 및 DB 업데이트
		for (int i = 1; i <= vo.getNumberOfPlayer(); i++) {
			pvo.setPseq(i);		//플레이어 번호 셋팅
			pvo.setGseq(gseq);	//게임 고유번호 셋팅
			
			if(i == 1) pvo.setName(vo.getFirstPlayer());			//첫번째 플레이어 이름 셋팅
			else pvo.setName(request.getParameter("player" + i));	//나머지 플레이어 이름 셋팅
			
			try {
				pService.player_insert(pvo);	//플레이어 정보 DB Insert
			} catch (Exception e) {
				System.out.println("newGame() PLAYER DB Insert Error!");
			}
		}
		
		return "redirect:loadGame?gseq=" + gseq;
	}
	
	// 해당 게임 정보 출력 메소드 (게임 정보, 플레이어별 점수 목록, 총점)
	// DB에 저장된 게임 정보, DB에 저장된 플레이어별 핀 점수 리스트, 핀 점수에 따른 각 프레임별 총점 리스트(map의 List 배열)
	@RequestMapping("/loadGame")
	public String loadGame(@RequestParam int gseq, Model model) {
		try {
			list = pService.all_list(gseq);		//DB에 저장된 모든 플레이어의 핀 점수 리스트 불러옴
			gvo = gService.game_info(gseq);		//DB에 저장된 게임 정보 불러옴
			gvo.setPlayerName(list.get(gvo.getPseq()-1).getName());
			// → 핀을 입력할 차례인 플레이어의 이름 초기화
		} catch (Exception e) {
			System.out.println("loadGame() DB Connect Error!");
		}
		
		PrintScoreBoard board = new PrintScoreBoard();
		ArrayList<HashMap<String, Object>> printMap = new ArrayList<HashMap<String,Object>>();
		
		for (PlayerVO vo : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map = board.scoreBoard(vo, gvo);
			// → DB에 저장되어있는 게임 정보와 핀 점수들을 넘겨
			//	  각 프레임 별 총점 계산 / 계산된 총점 리스트, 마지막 총점, 보너스 리스트, 플레이어 순서(gvo 플레이어 순서와 다름) 반환
			
			printMap.add(map);
		}

		model.addAttribute("list", list);
		model.addAttribute("printMap", printMap);
		model.addAttribute("gvo", gvo);
		
		return "scoreBoard";	
	}

	@ResponseBody @RequestMapping("/selfTest")
	public ArrayList<HashMap<String, Object>> selfTest(int selector, int gseq) {
		Integer ball_10_1 = pService.all_list(gseq).get(gvo.getPseq()-1).getBall_10_1();
		// → 10프레임의 1투구가 이미 업데이트되어있는 경우 할당
		
		try {
			//gService.game_reset(gseq);		//게임 정보 및 플레이어 점수 reset
			gvo = gService.game_info(gseq);		//DB에 저장된 게임 정보 불러옴
		} catch (Exception e) {
			System.out.println("selfTest() DB Reset Error!");
		}
		
		while (true) {
			if(gvo.getProceeding().equals("N")) break;
			// → 게임이 종료되면 반복문을 벗어남
			
			int roll = 0;
			
			if(selector == 1) {		//All Spare
				Random random = new Random();
				if(gvo.getTurn() == 1) {
					roll = random.nextInt(gvo.getRemainPin());
					// → 첫번째 투구는 10을 제외한 0부터 9까지의 랜덤 핀 값으로 초기화
				} else if(gvo.getTurn() == 2) {
					if(gvo.getFrame() == 10 && ball_10_1 == 10) {
						roll = random.nextInt(gvo.getRemainPin());
						// → 10프레임의 1투구가 스트라이크일 때 0부터 9까지의 랜덤 핀 값으로 초기화
					} else {
						roll = gvo.getRemainPin();
						// → 그 외의 경우는 두번째 투구는 남아있는 핀 값으로 초기화(10프레임 제외)
					}
				} else if(gvo.getTurn() == 3 && gvo.getRemainPin() != 10) {
					roll = gvo.getRemainPin();
					// → 10프레임의 2,3투구가 스페어야하는 경우 남아있는 핀 값으로 초기화
				} else {
					roll = random.nextInt(gvo.getRemainPin() + 1);
					// → 세번째 투구가 0부터 10까지 올 수 있는 경우(10|10|? / 6|4|?)는  0부터 10까지 랜덤 핀 값으로 초기화
				}
			} else if(selector == 2) {	//Perpect
				roll = 10;
			} else {		//Random
				Random random = new Random();
				roll = random.nextInt(gvo.getRemainPin() + 1);
			}
			
			try {
				gvo.setRoll(roll);
				pService.update_roll(gvo);						
				// → 플레이어 점수 DB 업데이트
				
				if(gvo.getFrame() == 10) {
					list = pService.all_list(gseq);
					ball_10_1 = list.get(gvo.getPseq()-1).getBall_10_1();
					// →  플레이어 점수 DB 업데이트 되기전까지는 
					//	 10프레임의 1투구가 DB에 저장되어 있지 않은 경우, 업데이트 후 저장
				}
				
				gvo = game_info.gameInfoCalc(gvo, roll, ball_10_1);		//게임관련 정보 계산
				gService.game_update(gvo);		//계산된 게임 정보 DB 업데이트
			} catch (Exception e) {
				System.out.println("selfTest() DB Update Error!");
			}
			
		} //while
		
		try {
			list = pService.all_list(gseq);		//DB에 저장된 모든 플레이어의 핀 점수 리스트 불러옴
			gvo = gService.game_info(gseq);		//DB에 저장된 게임 정보 불러옴
		} catch (Exception e) {
			System.out.println("selfTest() DB Connect Error!");
		}
		
		PrintScoreBoard board = new PrintScoreBoard();
		
		ArrayList<HashMap<String, Object>> printMap = new ArrayList<HashMap<String,Object>>();
		// → 플레이어가 여러명이므로 다수의 map을 보내기 위해 map 리스트를 선언
		
		// 현재 게임이 진행중인 플레이어의 이름과 플레이어 번호 반환
		for (PlayerVO vo : list) {
			// 각 프레임 별 총점 계산 + 계산된 총점 리스트 반환
			HashMap<String, Object> map = new HashMap<String, Object>();
			ArrayList<Integer> pinList = new ArrayList<Integer>();	//핀 점수 리스트
			
			// 핀 점수 리스트 
			for (int i = 1; i <= 10; i++) {
				pinList.add(vo.get_Ball(i, 1));
				pinList.add(vo.get_Ball(i, 2));
				if(i == 10) pinList.add(vo.getBall_10_3());
			}
			
			map = board.scoreBoard(vo, gvo);
			// → 마지막으로 저장된 게임 정보까지 총점 계산 + 총점 리스트 반환 / 최종 총점 / 보너스 리스트 / 플레이어 순서 반환
			
			map.put("pinList", pinList);
			// → 뷰 화면에 출력하기 쉽게 vo가 아닌 정수형 배열을 map에 추가
			
			printMap.add(map);	// map을 배열에 추가
			
			if(gvo.getFrame() == 11) {	
				map.put("gseq", gseq);
				map.put("gameover", "Y");
				pService.update_totalScore(map);	//총점 DB 업데이트
			}
			
			vo.setGameover("Y");
			// → 모든 플레이어가 입력이 끝났으므로 Y로 초기화(스코어보드 이벤트 알림 메시지 출력을 위해)
		}
		
		//gvo.setPlayerName(list.get(gvo.getPseq()-1).getName());		
		gvo.setTotalScore(0);	//총점 DB 업데이트 시점을 위해 0으로 초기화(입력 완료)
		
		return printMap;
	}
	
	// 핀을 굴릴 때 실행되는 메소드
	@ResponseBody @RequestMapping("/roll")
	public HashMap<String, Object> roll(int gseq, int rollNum, Model model) {
		
		gvo.setRoll(rollNum);
		
		PlayerVO pvo = null;
		PrintScoreBoard board = new PrintScoreBoard();
		HashMap<String, Object> printMap = new HashMap<String, Object>();
		
		try {
			pService.update_roll(gvo);			//핀 점수 업데이트
			list = pService.all_list(gseq);		//DB에 저장된 모든 플레이어의 핀 점수 리스트 불러옴
			
			pvo = list.get(gvo.getPseq()-1);
			printMap = board.scoreBoard(pvo, gvo);
			// → 계산하기전 게임 정보와 핀 점수를 넘겨줌으로써 총점 계산 + 총점 리스트 반환 / 마지막 총점 / 보너스 리스트 / 플레이어 번호 반환
			
			gvo = game_info.gameInfoCalc(gvo, gvo.getRoll(), pvo.getBall_10_1());	//게임 정보 계산
			gService.game_update(gvo);	//계산된 게임 정보 DB 업데이트
			
			if(gvo.getTotalScore() != null && gvo.getTotalScore() == 0) {
				printMap.put("gseq", gseq);	
				
				pService.update_totalScore(printMap);	//총점 DB 업데이트
				// → gvo가 아닌 map을 넘겨주는 이유는 gvo의 pseq가 업데이트하고자 하는 pseq와 다르기 때문이다.
				
				gvo.setTotalScore((Integer)printMap.get("totalScore"));
				// → 최종 총점을 DB에 업데이트했으므로 gvo에 해당 총점을 셋팅(스코어보드 이벤트 메시지 출력을 위해)
			}
		} catch (Exception e) {
			System.out.println("roll() DB Update Error!");
		}

		if(pvo != null) gvo.setPlayerName(list.get(gvo.getPseq()-1).getName());
		
		printMap.put("gvo", gvo);	//계산된 게임 정보를 map에 추가
		
		return printMap;
	}
	
	
	@ResponseBody @RequestMapping("/reset")
	public GameVO reset(int gseq) {
		try {
			gService.game_reset(gseq);			//게임 정보 DB 초기화
			gvo = gService.game_info(gseq);		//DB에 저장된 게임 정보 불러옴
		} catch (Exception e) {
			System.out.println("reset() DB reset Error!");
		}
		
		gvo.setPlayerName(gvo.getFirstPlayer());	//플레이어 이름 셋팅
		
		return gvo;
	}
	
	@ResponseBody @RequestMapping("/delete")
	public HashMap<String, Object> delete(int gseq) {
		HashMap<String, Object> printMap = new HashMap<String, Object>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();	
		// → 총점 DB 업데이트 시 필요한 정보를 보내기 위한 map
		
		PrintScoreBoard board = new PrintScoreBoard();
		PlayerVO pvo = null;

		if(gvo != null && list != null) {
			gvo = game_info.gameInfoDeleteCalc(gvo, list);	//삭제 시 게임관련 정보 계산
		}
		
		if(gvo != null) {
			map.put("gseq", gvo.getGseq());
			map.put("pseq", gvo.getPseq());
			
			try {
				gService.game_update(gvo);		//계산된 게임 정보 업데이트
				pService.delete_frame(gvo);		//DB에 저장된 플레이어 별 점수 삭제
				
				if(gvo.getTotalScore() != null && gvo.getTotalScore() == 1) {
				// → 각 플레이어별 마지막 투구가 삭제되었을 때
					
					map.put("totalScore", null);		
					// → 총점 부분을 null값으로 초기화하기 위해 map의 totalScore를 null값으로 추가
					
					pService.update_totalScore(map);	//총점 DB 업데이트
				}
				
				list = pService.all_list(gseq);
				// → DB에 저장된 모든 플레이어의 핀 점수 리스트 불러옴
				
				pvo = list.get(gvo.getPseq()-1);
				if(pvo.getTotal_score() == null) gvo.setTotalScore(null);
				// → 스코어보드 이벤트 메시지 출력을 위해 gvo의 총점을 null로 초기화
				
				printMap = board.scoreBoard(pvo, gvo);
				// → 계산된 게임 정보와 핀 점수를 넘겨줌으로써 총점 계산 + 총점 리스트 / 보너스 리스트 / 마지막 총점 / 플레이어 번호 반환
			} catch (Exception e) {
				System.out.println("delete() DB reset Error!");
			}
			
			gvo.setPlayerName(pvo.getName());
			printMap.put("gvo", gvo);
		} else {
			printMap.put("gvo", gvo);			
			// → 초기화하기전 null값을 화면단으로 보냄(삭제할 핀이 존재하지 않는 경우)
			
			gvo = gService.game_info(gseq);		
			// → gvo가 null일 경우 DB에 있는 정보로 다시 초기화
		}
		
		return printMap;
	}
	
	@RequestMapping("/resetRank")
	public String resetRank(Model model) {
		List<PlayerVO> ranking_list = null;
		
		try {
			ranking_list = pService.ranking_list();
			// → 랭킹 리스트 불러오기
		} catch (Exception e) {
			System.out.println("resetRank() DB Connect Error!");
		} 

		model.addAttribute("ranking", ranking_list);
		
		return "include/ranking";
	}
	
	@ResponseBody @RequestMapping(value = {"updateRoll"}, method = {RequestMethod.POST})
	public HashMap<String, Object> updateRoll(@RequestBody GameVO gamevo) {
		HashMap<String, Object> printMap = new HashMap<String, Object>();
		PrintScoreBoard board = new PrintScoreBoard();
		
		GameVO prevgvo = new GameVO();
		prevgvo.setPseq(gamevo.getPseq());
		prevgvo.setFrame(gamevo.getFrame());
		prevgvo.setTurn(gamevo.getTurn());
		prevgvo.setRoll(gamevo.getRoll());
		prevgvo.setGseq(gamevo.getGseq());
		//→ 업데이트 관련 게임 정보 셋팅
		
		PlayerVO playervo = list.get(prevgvo.getPseq()-1);
		// → 수정한 점수가 셋팅된 핀 점수 리스트를 총점 계산에 넘겨주기 위해 선언한 PlayerVO
		
		updateGvo.add(prevgvo);		
		// → 수정이 완료된 후 한꺼번에 DB 업데이트를 하기 위해 GameVO 리스트에 추가시킴
		
		for (GameVO gvo : updateGvo) {
			playervo.set_Ball(gvo.getFrame(), gvo.getTurn(), gvo.getRoll());
			// → 추가된 GameVO에 따라 플레이어 정보 중 핀 점수를 셋팅
		}
		
		printMap = board.scoreBoard(playervo, gvo);
		// → 기존에 저장되어 있는 게임 정보와 수정된 핀 점수들을 넘겨줌으로써
		//	 해당 플레이어의 총점 계산 + 총점 리스트 / 마지막 총점 / 보너스 리스트 / 플레이어 번호 반환 
		
		gamevo = game_info.gameInfoCalc(gamevo, gamevo.getRoll(), playervo.getBall_10_1());
		// → 수정 관련 게임 정보에 따라 게임 정보 계산

		if( (gamevo.getNumberOfPlayer() == 1 && prevgvo.getFrame() != gamevo.getFrame()) ||
			(gamevo.getNumberOfPlayer() != 1 && prevgvo.getPseq() != gamevo.getPseq())) {
		// → 계산된 게임 정보에 따라 핀 점수 수정이 끝났는 지 아닌 지를 판별
			
			//---------------------수정 시 null값으로 초기화 하기 위한 작업
			GameVO vo = new GameVO();	
			vo.setPseq(prevgvo.getPseq());
			vo.setGseq(prevgvo.getGseq());
			vo.setRoll(null);
			
			if(prevgvo.getRoll() == 10 && prevgvo.getTurn() == 1) {		
			// → 수정할 점수가 스트라이크일 경우 2투구를 null로 초기화(1 ~ 9프레임까지)
				vo.setFrame(prevgvo.getFrame());
				vo.setTurn(2);
				pService.update_roll(vo);	//핀 점수 업데이트
			}
			
			if(prevgvo.getFrame() == 10 && prevgvo.getTurn() == 2) {	
			// → 수정전에 10프레임의 3투구가 null이 아닐 경우를 위해 null값으로 DB를 초기화
				vo.setFrame(10);
				vo.setTurn(3);
				pService.update_roll(vo);	//핀 점수 업데이트
			}
			//----------------------------------------------------
			
			if(gamevo.getProceeding().equals("Y")) {	
			// → 마지막 플레이어의 10프레임 수정이 아닐 경우, 즉 1~9프레임을 수정할 경우
				gamevo.setProceeding(gvo.getProceeding());
				// → 일반 게임 모드에서 게임이 종료된 상태일 경우 proceeding 변수를 'N'으로 초기화 
			}
			
			for (GameVO gvo : updateGvo) {
				pService.update_roll(gvo);	//핀 점수 수정이 끝났을 때 수정된 핀 점수들을 DB에 업데이트 한다
			}
			
			list = pService.all_list(gamevo.getGseq());		//업데이트된 모든 플레이어의 핀 점수 리스트 불러옴
			
			Integer totalScore = list.get(playervo.getPseq()-1).getTotal_score();
			// → 수정한 플레이어의 DB 정보에서 총점을 저장하는 변수
			
			if(totalScore != null) {	
			// → 플레이어의 DB 정보에서 총점이 저장되어 있는 경우, 
			//	 즉  마지막 투구까지 입력이 완료된 상태인 경우(이때 총점은 업데이트 되지 않은 총점)
				printMap.put("gseq", gamevo.getGseq());
				
				pService.update_totalScore(printMap);	//총점 DB 업데이트
				
				gamevo.setTotalScore((Integer)printMap.get("totalScore"));
				// → 스코어보드 이벤트 문구 출력을 위해 게임정보의 총점 셋팅
			}
			gamevo.setPlayerName(gvo.getPlayerName());
			
			updateGvo = new ArrayList<GameVO>();
			// 수정이 끝났을 경우에는 GameVO 리스트(전역변수)를 초기화
		}
		
		printMap.put("gvo", gamevo);
		
		return printMap;
	}
}
