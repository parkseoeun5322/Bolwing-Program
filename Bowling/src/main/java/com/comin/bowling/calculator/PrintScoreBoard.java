package com.comin.bowling.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.comin.bowling.vo.PlayerVO;

/*
 * 해당 플레이어의 총점 리스트, 플레이어 번호, 
 * 각 프레임당 보너스 여부를 저장하는 리스트, 마지막 총점을 Map으로 반환
 */

public class PrintScoreBoard {
	public HashMap<String, Object> scoreBoard(PlayerVO vo, int frame, int pseq, int turn, int numberOfPlayer) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		
		if(frame == 11) {
			frame = 10; 
			pseq = numberOfPlayer;
		}
			
		Score score = new Score();
		score.initialization();		//초기화
		
		for (int i = 1; i <= 10; i++) {
			score.roll(vo.get_Ball(i, 1));
			score.roll(vo.get_Ball(i, 2));
			if(i == 10) score.roll(vo.getBall_10_3());
		}
		
		scoreList = score.score(frame);
		
		map.put("pseq", vo.getPseq());
		map.put("bonusList", score.isBonus(frame, turn));
		map.put("scoreList", scoreList);
		
		for (int i = 0; i < 10; i++) {
			Integer totalScore = null;
			if(scoreList.get(i) != null) {
				totalScore = scoreList.get(i);
				map.put("totalScore", totalScore);
			}
		}

		return map;
	}
}
