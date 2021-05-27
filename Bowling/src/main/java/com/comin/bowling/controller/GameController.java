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
		List<GameVO> game_list = null;
		ArrayList<GameVO> proceeding = new ArrayList<GameVO>();
		ArrayList<GameVO> gameover = new ArrayList<GameVO>();
		
		try {
			game_list = gService.game_list();
		} catch (Exception e) {
			System.out.println("main() DB Connect Error!");
		}
		
		
		for (GameVO gameVO : game_list) {
			if(gameVO.getProceeding().equals("Y")) {
				proceeding.add(gameVO);
			} else {
				gameover.add(gameVO);
			}
		}
		
		model.addAttribute("game", game_list);
		model.addAttribute("proceeding", proceeding);
		model.addAttribute("gameover", gameover);
		
		return "main";			
	}
	
	// 게임 생성 메소드 (게임 정보, 플레이어 정보 DB Insert)
	@RequestMapping("/newGame")
	public String newGame(GameVO vo, HttpServletRequest request) {
		PlayerVO pvo = new PlayerVO();
		int gseq = 0;
		
		try {
			gseq = gService.game_insert(vo);	//게임 정보 DB Insert & 해당 게임 시퀀스 반환
		} catch (Exception e) {
			System.out.println("newGame() GAME DB Insert Error!");
		}
		
		// PlayerVO 셋팅 및 DB 업데이트
		for (int i = 1; i <= vo.getNumberOfPlayer(); i++) {
			pvo.setPseq(i);
			pvo.setGseq(gseq);
			
			if(i == 1) pvo.setName(vo.getFirstPlayer());
			else pvo.setName(request.getParameter("player" + i));
			
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
			// 핀을 입력할 차례인 플레이어의 이름 초기화
		} catch (Exception e) {
			System.out.println("loadGame() DB Connect Error!");
		}
		
		PrintScoreBoard board = new PrintScoreBoard();
		ArrayList<HashMap<String, Object>> printMap = new ArrayList<HashMap<String,Object>>();
		
		for (PlayerVO vo : list) {
			// 각 프레임 별 총점 계산 + 계산된 총점 리스트 / 마지막 총점 / 보너스 리스트 / 플레이어 순서 반환
			HashMap<String, Object> map = new HashMap<String, Object>();
			map = board.scoreBoard(vo, gvo.getFrame(), gvo.getPseq(), gvo.getTurn(), gvo.getNumberOfPlayer());
			printMap.add(map);
		}

		model.addAttribute("list", list);
		model.addAttribute("printMap", printMap);
		model.addAttribute("gvo", gvo);
		
		return "scoreBoard2";	
	}

	@ResponseBody @RequestMapping("/selfTest")
	public ArrayList<HashMap<String, Object>> selfTest(int selector, int gseq) {
		Integer ball_10_1 = null;
		
		try {
			gService.game_reset(gseq);			//게임 정보 및 플레이어 점수 reset
			gvo = gService.game_info(gseq);		//DB에 저장된 게임 정보 불러옴
		} catch (Exception e) {
			System.out.println("selfTest() DB Reset Error!");
		}
		
		while (true) {
			if(gvo.getProceeding().equals("N")) break;
			
			int roll = 0;
			if(selector == 1) {		//All Spare
				roll = 5;
			} else if(selector == 2) {	//Perpect
				roll = 10;
			} else {		//Random
				Random random = new Random();
				roll = random.nextInt(gvo.getRemainPin() + 1);
			}
			try {
				gvo.setRoll(roll);
				pService.update_roll(gvo);						//플레이어 점수 DB 업데이트
				
				if(gvo.getFrame() == 10 && gvo.getTurn() == 1) ball_10_1 = roll;
				gvo = game_info.gameInfoCalc(gvo, roll, ball_10_1);		//게임관련 정보 계산
				gService.game_update(gvo);						//게임 정보 DB 업데이트
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
		
		// 현재 플레이가 진행중인 플레이어의 이름과 플레이어 번호 반환
		for (PlayerVO vo : list) {
			// 각 프레임 별 총점 계산 + 계산된 총점 리스트 반환
			HashMap<String, Object> map = new HashMap<String, Object>();
			ArrayList<Integer> pinList = new ArrayList<Integer>();
			for (int i = 1; i <= 10; i++) {
				pinList.add(vo.get_Ball(i, 1));
				pinList.add(vo.get_Ball(i, 2));
				if(i == 10) pinList.add(vo.getBall_10_3());
			}
			
			map = board.scoreBoard(vo, gvo.getFrame(), gvo.getPseq(), gvo.getTurn(), gvo.getNumberOfPlayer());
			map.put("pinList", pinList);
			printMap.add(map);
			
			if(gvo.getFrame() == 11) {
				map.put("gseq", gseq);
				map.put("gameover", "Y");
				pService.update_totalScore(map);	//총점 DB 업데이트
			}
			vo.setGameover("Y");
		}
		
		gvo.setPlayerName(list.get(gvo.getPseq()-1).getName());
		gvo.setTotalScore(0);
		
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
			//pvo = pService.player_list(map);
			pvo = list.get(gvo.getPseq()-1);
			printMap = board.scoreBoard(pvo, gvo.getFrame(), gvo.getPseq(), gvo.getTurn(), gvo.getNumberOfPlayer());
			gvo = game_info.gameInfoCalc(gvo, gvo.getRoll(), pvo.getBall_10_1());		//게임관련 정보 계산
			gService.game_update(gvo);								//계산된 게임 정보 업데이트
			
			if(gvo.getTotalScore() != null && gvo.getTotalScore() == 0) {
				printMap.put("gseq", gseq);
				pService.update_totalScore(printMap);	//총점 DB 업데이트
				// gvo가 아닌 map을 매개변수로 사용한 이유는 gvo의 pseq가 업데이트하고자 하는 pseq와 다르기 때문이다.
				//System.out.println("roll() 총점 : " + printMap.get("totalScore"));
				gvo.setTotalScore((Integer)printMap.get("totalScore"));
			}
			
		} catch (Exception e) {
			System.out.println("roll() DB Update Error!");
		}

		//gvo.setTotalScore((Integer) printMap.get("totalScore"));
		if(pvo != null) gvo.setPlayerName(list.get(gvo.getPseq()-1).getName());
		
		printMap.put("gvo", gvo);
		
		//return gvo;
		return printMap;
	}
	
	
	@ResponseBody @RequestMapping("/reset")
	public GameVO reset(int gseq) {
		try {
			gService.game_reset(gseq);
			gvo = gService.game_info(gseq);		//DB에 저장된 게임 정보 불러옴
		} catch (Exception e) {
			System.out.println("reset() DB reset Error!");
		}
		gvo.setPlayerName(gvo.getFirstPlayer());
		//return "redirect:loadGame?gseq=" + gseq;
		return gvo;
	}
	
	@ResponseBody @RequestMapping("/delete")
	public HashMap<String, Object> delete(int gseq) {
		HashMap<String, Object> printMap = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		PrintScoreBoard board = new PrintScoreBoard();
		PlayerVO pvo = null;

		if(gvo != null && list != null) {
			gvo = game_info.gameInfoDeleteCalc(gvo, list);	//삭제 시 게임관련 정보 계산
		}
		
		if(gvo != null) {
			map.put("gseq", gvo.getGseq());
			map.put("pseq", gvo.getPseq());
			
			try {
				gService.game_update(gvo);				//계산된 게임 정보 업데이트
				pService.delete_frame(gvo);				//DB에 저장된 플레이어 별 점수 삭제
				if(gvo.getTotalScore() != null) {
					if(gvo.getTotalScore() == 1) {
						map.put("totalScore", null);
						pService.update_totalScore(map);	//총점 DB 업데이트
					}
				}
				list = pService.all_list(gseq);			//DB에 저장된 모든 플레이어의 핀 점수 리스트 불러옴
				pvo = list.get(gvo.getPseq()-1);
				if(pvo.getTotal_score() == null) gvo.setTotalScore(null);
				printMap = board.scoreBoard(pvo, gvo.getFrame(), gvo.getPseq(), gvo.getTurn(), gvo.getNumberOfPlayer());
			} catch (Exception e) {
				System.out.println("delete() DB reset Error!");
			}
			//System.out.println("delete () 총점 : " + gvo.getTotalScore());
			
			
			//gvo.setTotalScore((Integer)printMap.get("totalScore"));
			gvo.setPlayerName(pvo.getName());
			printMap.put("gvo", gvo);
		} else {
			printMap.put("gvo", gvo);			// 초기화 하기전 null값을 화면단으로 보냄
			gvo = gService.game_info(gseq);		//gvo가 null일 경우 DB에 있는 정보로 다시 초기화
		}
		
		
		return printMap;
	}
	
	@RequestMapping("/resetRank")
	public String resetRank(Model model) {
		List<PlayerVO> ranking_list = null;
		try {
			ranking_list = pService.ranking_list();		//랭킹 리스트 불러오기
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

		//System.out.println("gseq : " + gamevo.getGseq());
//		int prevPseq = gamevo.getPseq();
//		int prevFrame = gamevo.getFrame();
//		int prevTurn = gamevo.getTurn();
		prevgvo.setPseq(gamevo.getPseq());
		prevgvo.setFrame(gamevo.getFrame());
		prevgvo.setTurn(gamevo.getTurn());
		prevgvo.setRoll(gamevo.getRoll());
		prevgvo.setGseq(gamevo.getGseq());
		
		PlayerVO playervo = list.get(prevgvo.getPseq()-1);
		
//		System.out.println("frame : " + gamevo.getFrame());
//		System.out.println("turn : " + gamevo.getTurn());
//		System.out.println("pseq : " + gamevo.getPseq());
//		System.out.println("pseq : " + gamevo.getRemainPin());
//		System.out.println("----------------------------------");
		updateGvo.add(prevgvo);		//수정이 완료된 후 한꺼번에 DB 업데이트
		
		for (GameVO gvo : updateGvo) {
			playervo.set_Ball(gvo.getFrame(), gvo.getTurn(), gvo.getRoll());
		}
		printMap = board.scoreBoard(playervo, gvo.getFrame(), gvo.getPseq(), gvo.getTurn(), gvo.getNumberOfPlayer());

		gamevo = game_info.gameInfoCalc(gamevo, gamevo.getRoll(), playervo.getBall_10_1());
//		System.out.println("계산 후 frame : " + gamevo.getFrame());
//		System.out.println("계산 후 turn : " + gamevo.getTurn());
//		System.out.println("계산 후 pseq : " + gamevo.getPseq());
//		System.out.println("계산 후 pseq : " + gamevo.getRemainPin());
		//System.out.println("gamevo 게임 진행 여부 : " + gamevo.getProceeding());
		//System.out.println("gvo 게임 진행 여부 : " + gvo.getProceeding());

		// 핀 점수 수정이 끝났을 경우를 위해 게임 진행 여부와 다음 차례의 플레이어 이름 정보를 전달
		if( (gamevo.getNumberOfPlayer() == 1 && prevgvo.getFrame() != gamevo.getFrame()) ||
			(gamevo.getNumberOfPlayer() != 1 && prevgvo.getPseq() != gamevo.getPseq())) {
			
			GameVO vo = new GameVO();	
			vo.setPseq(prevgvo.getPseq());
			vo.setGseq(gamevo.getGseq());
			vo.setRoll(null);
			//수정 시 null값으로 초기화 하기 위한 작업
			
			if(prevgvo.getRoll() == 10 && prevgvo.getTurn() == 1) {		//수정할 점수가 스트라이크일 경우 2투구를 null로 초기화
				vo.setFrame(prevgvo.getFrame());
				vo.setTurn(2);
				pService.update_roll(vo);	//핀 점수 업데이트
			}
			
			if(prevgvo.getFrame() == 10 && prevgvo.getTurn() == 2) {	//수정전에 10프레임의 3투구가 null이 아닐 경우를 위해 null값으로 DB를 초기화
				vo.setFrame(10);
				vo.setTurn(3);
				pService.update_roll(vo);	//핀 점수 업데이트
			}
			
			if(gamevo.getProceeding().equals("Y")) {	//마지막 플레이어의 10프레임 수정이 아닐 경우
				gamevo.setProceeding(gvo.getProceeding());
			}
			
			for (GameVO gvo : updateGvo) {
				//System.out.println("플레이어 : " + gvo.getPseq() + " / 프레임 : " + gvo.getFrame() + " / 턴 : " + gvo.getTurn());
				pService.update_roll(gvo);		//핀 점수 수정이 끝났을 때 핀 점수 업데이트
			}
			
			list = pService.all_list(gamevo.getGseq());		//업데이트된 모든 플레이어의 핀 점수 리스트 불러옴
			
			Integer totalScore = list.get(playervo.getPseq()-1).getTotal_score();
			if(totalScore != null) {	//이전에 이미 입력이 완료되어 최종 총점계산이 끝난 경우 (이때 totalScore는 업데이트 되지 않은 총점)
				printMap.put("gseq", gamevo.getGseq());
				pService.update_totalScore(printMap);	//총점 DB 업데이트
				gamevo.setTotalScore((Integer)printMap.get("totalScore"));
			}
			gamevo.setPlayerName(gvo.getPlayerName());
			updateGvo = new ArrayList<GameVO>();
		}
		
		printMap.put("gvo", gamevo);
		
		return printMap;
	}

}
