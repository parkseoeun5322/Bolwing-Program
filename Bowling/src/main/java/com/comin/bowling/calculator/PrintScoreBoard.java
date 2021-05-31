package com.comin.bowling.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.comin.bowling.vo.GameVO;
import com.comin.bowling.vo.PlayerVO;

/*
 * PrintScoreBoard.java : 해당 플레이어의 총점 리스트, 현재 게임을 진행중인 플레이어 번호, 
 * 각 프레임당 보너스 여부를 저장하는 리스트, 마지막 총점을 Map으로 묶어 반환
 */

public class PrintScoreBoard {
	public HashMap<String, Object> scoreBoard(PlayerVO pvo, GameVO gvo) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		int frame = gvo.getFrame();
		int pseq = gvo.getPseq();
		int turn = gvo.getTurn();
		int numberOfPlayer = gvo.getNumberOfPlayer();
		
		if(frame == 11) {
			frame = 10; 
			pseq = numberOfPlayer;
		}

		Score score = new Score();
		score.initialization();		//Score 관련 변수 초기화
		
		for (int i = 1; i <= 10; i++) {		//roll()를 통해 핀 점수 리스트 초기화
			score.roll(pvo.get_Ball(i, 1));
			score.roll(pvo.get_Ball(i, 2));
			if(i == 10) score.roll(pvo.getBall_10_3());
		}
		
		scoreList = score.score(frame);
		// → score() > 프레임 번호를 넘겨줌으로써 해당 프레임까지의 총점 리스트를 반환
		
		map.put("pseq", pvo.getPseq());
		// → 해당 플레이어 번호를 map에 추가(스코어보드 총점 출력시 해당  gvo의 플레이어 번호와의 비교를 위해)
		
		map.put("bonusList", score.isBonus(frame, turn));
		// → isBonus() > 프레임 번호화 투구 번호를 넘겨줌으로써 해당 프레임까지의 보너스 리스트를 가져오고 map에 추가
		
		map.put("scoreList", scoreList);
		
		for (int i = 0; i < 10; i++) {
			Integer totalScore = null;
			if(scoreList.get(i) != null) {
				totalScore = scoreList.get(i);
				// → 가장 마지막으로 저장된 총점을 변수에 초기화
				
				map.put("totalScore", totalScore);
			}
		}
		return map;
	}
}
