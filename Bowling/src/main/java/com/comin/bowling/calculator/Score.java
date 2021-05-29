package com.comin.bowling.calculator;

import java.util.ArrayList;
import java.util.HashMap;

import com.comin.bowling.vo.GameVO;

public class Score {
	private ArrayList<Integer> pinList;
	int score;
	int frameNum;

	public void initialization() {
		score = 0; frameNum = 0;
		pinList = new ArrayList<Integer>();
	}
	
	public void roll(Integer pin) {
		pinList.add(pin);
	}

	public ArrayList<Integer> score(int frame) {
		ArrayList<Integer> scoreList = new ArrayList<Integer>();

		for (int i = 0; i < frame; i++) {
			if (isStrike(frameNum)) {
				score += 10;
				Integer bonus = nextTwoBallsForStrike(frameNum, frame);
				if (bonus == null) {
					scoreList.add(null);
				} else {
					score += bonus;
					scoreList.add(score);
				}
			} else if (isSpare(frameNum)) {
				score += 10;
				Integer bonus = nextBallForSpare(frameNum);
				if (bonus == null) {
					scoreList.add(null);
				} else {
					score += bonus;
					scoreList.add(score);
				}
			} else {
				Integer bonus = nextBallsInFrame(frameNum);
				if (bonus == null) {
					scoreList.add(null);
				} else {
					score += bonus;
					scoreList.add(score);
				}
			}
			frameNum += 2;
		}
		
		for (int i = 0; i < 10 - frame; i++) {
			// 화면단에 10개짜리 배열을 보내기 위해 아직 진행되지 않은 프레임의 점수에 null값을 넣어준다.
			scoreList.add(null);
		}

		return scoreList;
	}
	
	public ArrayList<String> isBonus(int frame, int turn) {
		ArrayList<String> bonusList = new ArrayList<String>();
		int frameNum = 0;
		for (int i = 0; i < frame; i++) {
			if(i < 9) {
				if(isStrike(frameNum)) {
					bonusList.add("isStrike1");
					bonusList.add(null);
				} else if(isSpare(frameNum)) {
					bonusList.add("");
					bonusList.add("isSpare");
				} else {
					bonusList.add("");
					bonusList.add("");
				}
			} else {
				// 10프레임의 1투구
				if(pinList.get(18) != null) {
					if(pinList.get(18) == 10) {
						bonusList.add("isStrike");
					} else {
						bonusList.add("");
					}
				} 
				
				// 10프레임의 2투구
				if(pinList.get(19) != null) {
					if(pinList.get(19) == 10 && pinList.get(18) == 10) {
						//0 10 x일 경우 두번째 투구는 스트라이트 X
						bonusList.add("isStrike"); 
					} else if(pinList.get(18) + pinList.get(19) == 10 && pinList.get(19) != 0) {
						//10 0 x일 경우 두번째 투구는 스페어 X
						bonusList.add("isSpare");
					}
					else bonusList.add("");
				}
				
				// 10프레임의 3투구
				if(pinList.get(20) != null) {
					if(pinList.get(20) == 10 && pinList.get(19) != 0) {
						//10 0 10일 경우 세번째 투구는 스트라이크 X
						bonusList.add("isStrike");
					} else if(pinList.get(19) + pinList.get(20) == 10
							&& pinList.get(20) != 0 
							&& (pinList.get(18) + pinList.get(19) != 10 || pinList.get(18) == 10) ) {
							//10 10 0 / 5 5 5 / 10 0 10일 경우 세번째 투구는 스페어 X
						bonusList.add("isSpare");
					} else {
						bonusList.add("");
					}
				}
			}
			frameNum += 2;
		}
		
		return bonusList;
	}

	private Integer nextBallsInFrame(int frameNum) {
		if (pinList.get(frameNum) == null || pinList.get(frameNum + 1) == null)
			return null;
		else
			return pinList.get(frameNum) + pinList.get(frameNum + 1);
	}

	private Integer nextBallForSpare(int frameNum) {
		if (pinList.get(frameNum + 2) == null)
			return null;
		else
			return pinList.get(frameNum + 2);
	}

	private Integer nextTwoBallsForStrike(int frameNum, int frame) {
		if (frameNum >= 18) { // 10 프레임이 스트라이크일 때
			if (pinList.get(19) == null || pinList.get(20) == null) return null;
			else return pinList.get(19) + pinList.get(20);
		} else if (pinList.get(frameNum + 2) == null || pinList.get(frameNum + 2) != 10 || frameNum >= 16) {
			//다음 프레임이 스트라이크가 아닐 때와	 9프레임이 스트라이크일 때																					
			if (pinList.get(frameNum + 2) == null || pinList.get(frameNum + 3) == null)
				return null;
			else
				return pinList.get(frameNum + 2) + pinList.get(frameNum + 3);
		} else { 	// 연속 스트라이크 일때
			if (pinList.get(frameNum + 2) == null || pinList.get(frameNum + 4) == null)
				return null;
			else
				return pinList.get(frameNum + 2) + pinList.get(frameNum + 4);
		}
	}

	private boolean isStrike(int frameNum) {
		if (pinList.get(frameNum) == null)
			return false;
		return pinList.get(frameNum) == 10;
	}

	private boolean isSpare(int frameNum) {
		if (pinList.get(frameNum) == null || pinList.get(frameNum + 1) == null)
			return false;
		return pinList.get(frameNum) + pinList.get(frameNum + 1) == 10;
	}
}
