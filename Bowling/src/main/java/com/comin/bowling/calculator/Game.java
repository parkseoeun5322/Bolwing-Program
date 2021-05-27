package com.comin.bowling.calculator;

import com.comin.bowling.vo.ScoreDTO;

public class Game {
	private ScoreDTO dto;
	int flag = 0;
	int thisIndex = 0;
	int currentIndex = 0;
	
	public Game(ScoreDTO dto) {
		this.dto = dto;
	}
	
	public ScoreDTO getDto() {
		return dto;
	}
	
	// 사용자에게 입력받을 때 프레임 점수 합이 10을 초과하는지 판별하는 메소드
	public boolean isAlert(int pin) {
		if(pin == 10) { return true; }
		else {
			flag += pin;
			//System.out.println("flag = " + flag);
			if(flag > 10) { flag -= pin; return false; }
			else { return true; }
		}
	}

	public void roll(int pin) {
		dto.setPin(pin);
		
		if(currentIndex > 20) currentIndex = 20;
		
		dto.setRoll(currentIndex);
		
		// 01 - 0 / 23 - 1 / 45 - 2 / 67 - 3
		dto.setFrameNum(dto.getRoll() / 2);				//프레임 번호 셋팅
		if(dto.getFrameNum() > 9) dto.setFrameNum(9);	//마지막 프레임 → 보너스 핀을 굴릴때 프레임 번호 고정
		
		dto.getScore_list()[dto.getRoll()] = pin;		//스코어 목록 저장
		
		if (isStrike(pin) && dto.getFrameNum() != 9) {	// 스트라이크 시
			dto.setRollIndex(1);	//투구번호는 1로 고정
			
			// 마지막 프레임을 제외하고 roll 횟수를 1씩 더 증가(18부터 10프레임)
			if (dto.getRoll() < 18) dto.setRoll(++currentIndex);
			
			//스트라이크인데 마지막 프레임이 아닐때
			setFrameList(pin, dto.getFrameNum()); //프레임 점수판 셋팅
			//setBonus(dto.getFrameNum());
			
		} else {	//스트라이크가 아닐 때
			dto.setFrameScore(dto.getFrameScore() + pin);
			if (dto.getRoll() % 2 == 1) {
				dto.setRollIndex(2);	//투구번호 셋팅
				setFrameList(dto.getFrameScore(), dto.getFrameNum()); 	//프레임 점수판 셋팅
				dto.setFrameScore(0);
			} else if(dto.getRoll() == 20) { dto.setRollIndex(3); 	//마지막 프레임의 보너스 핀 투구번호 처리
			} else dto.setRollIndex(1);
		}
		
		// 마지막 핀 보너스 점수 셋팅
		//if(isBonusRoll()) dto.bonus_list[9] = dto.score_list[20];
		if(isBonusRoll()) {
			dto.setTotalScore(dto.getTotalScore() + dto.getScore_list()[20]);
			dto.getTotal_list()[9] = dto.getTotalScore();
		}
		//print();
		setBonus(dto.getRoll());
		currentIndex++;
	}
	
	//마지막 프레임의 보너스 유무를 판별하기 위한 메소드
	public boolean isBonusRoll() {
		if (dto.getFrameNum() == 9) {
			if (dto.getFrame_list()[9] >= 10) return true;
		}
		return false;
	}
	
	// 총점을 구하는 메소드
	public int totalScore(int frameNum) {
		
		return dto.getTotalScore();
	}
	
	// 계산된 프레임 점수가 프레임 리스트에 저장
	public void setFrameList(int frameScore, int frameNum) {
		dto.getFrame_list()[frameNum] = frameScore;
		//setBonus(frameNum);
		flag = 0;	//프레임 리스트 저장 후 flag 변수(프레임 점수가 10을 초과하는 지 판별) 초기화
	}
	
	public void setBonus(int roll) {
		System.out.println("setBonus() : thisIndex = " + thisIndex + ", roll = " + roll
							+ "현재 스코어 : " + dto.getScore_list()[roll] + "그 다음 스코어 : " + dto.getScore_list()[roll + 1]);
		while (thisIndex <= roll) {
			int bonus = 0;
			if ( isStrike(roll) ) {
				//System.out.println("thisIndex = " + thisIndex + ", frameNum = " + frameNum);
				bonus = strikeBonus(thisIndex, roll);
			} else if ( isSpare(roll) ) {
				bonus = spareBonus(thisIndex, roll); 
			}
			
			if(bonus == -1) {
				return;
			}
			//dto.setTotalScore(dto.getTotalScore() + dto.getFrame_list()[thisIndex] + bonus);
			//System.out.println("현재 총 점수 : " + dto.getTotalScore());
			//dto.getTotal_list()[thisIndex] = dto.getTotalScore();
			thisIndex++;
		}
	} //setBonus()
	
	// 스페언 시 보너스 점수 계산 메소드
	private int spareBonus(int thisIndex, int roll) {
		System.out.println("spareBonus() : thisIndex = " + thisIndex + ", roll = " + roll);
		return -1;
		/*
		if (thisIndex < 9 && thisIndex + 1 > roll) {
			return -1;
		}
		
		//0-2 / 1-4 / 2-6 /
		return dto.getScore_list()[thisIndex * 2 + 2];
		*/
	}
	
	// 스트라이크 시 보너스 점수 계산 메소드
	private int strikeBonus(int thisIndex, int roll) {
		System.out.println("strikeBonus() : thisIndex = " + thisIndex + ", roll = " + roll);
		
		return -1;
		/*
		// 스트라이크가 처음 발생했을 때(thisIndex와 frameNum이 같음, 9프레임은 제외) 
		if (thisIndex < 18 && thisIndex + 1 > roll) {
		//if (thisIndex < 9 && thisIndex + 1 > frameNum) {
			System.out.println("경우1 : " + "thisIndex = " + thisIndex + ", roll = " + roll);
			return -1;
		}
		
		// 마지막 프레임일 때
		if (thisIndex == 9) {
			dto.setTotalScore(dto.getTotalScore() + dto.getScore_list()[18] + dto.getScore_list()[19]);
			System.out.println("경우 3 : " + "thisIndex = " + thisIndex + ", roll = " + roll);
			return -1;
		}
		
		// 그 다음 프레임이 스트라이크가 아닌 경우
		if (!isStrike(dto.getFrame_list()[thisIndex + 1])) {
			System.out.println("경우2 : " + "thisIndex = " + thisIndex + ", roll = " + roll + ", 보너스 = " + dto.getFrame_list()[thisIndex + 1]);
			return dto.getFrame_list()[thisIndex + 1];
		}
		
		//그 다음 프레임이 스트라이크인 경우 보너스 계산을 보류
		if (thisIndex <= 8 && thisIndex + 2 > roll) {
			System.out.println("경우4 : " + "thisIndex = " + thisIndex + ", frameNum = " + roll);
			return -1;
		}
		
		return 10 + dto.getScore_list()[thisIndex * 2 + 4];
		*/
	}
	
	/*
	// 보너스 점수 계산
	public void setBonus(int frameNum) {
		while (thisIndex <= frameNum) {
			int bonus = 0;
			if (isStrike(dto.getScore_list()[thisIndex * 2])) {
				//System.out.println("thisIndex = " + thisIndex + ", frameNum = " + frameNum);
				bonus = strikeBonus(thisIndex, frameNum);
			} else if (isSpare(dto.getFrame_list()[thisIndex])) {
				bonus = spareBonus(thisIndex, frameNum); 
			}
			
			if(bonus == -1) {
				return;
			}
			dto.setTotalScore(dto.getTotalScore() + dto.getFrame_list()[thisIndex] + bonus);
			//System.out.println("현재 총 점수 : " + dto.getTotalScore());
			dto.getTotal_list()[thisIndex] = dto.getTotalScore();
			thisIndex++;
		}
	} //setBonus()
	
	// 스페언 시 보너스 점수 계산 메소드
	private int spareBonus(int thisIndex, int frameNum) {
		//System.out.println("thisIndex = " + thisIndex + ", frameNum = " + frameNum + ", bonus = " + score_list[thisIndex * 2 + 2]);
		if (thisIndex < 9 && thisIndex + 1 > frameNum) return -1;
		
		//0-2 / 1-4 / 2-6 /
		return dto.getScore_list()[thisIndex * 2 + 2];
	}
	
	// 스트라이크 시 보너스 점수 계산 메소드
	private int strikeBonus(int thisIndex, int frameNum) {
		// 스트라이크가 처음 발생했을 때(thisIndex와 frameNum이 같음, 9프레임은 제외) 
		if (thisIndex < 9 && thisIndex + 1 > frameNum) {
		//if (thisIndex < 9 && thisIndex + 1 > frameNum) {
			System.out.println("경우1 : " + "thisIndex = " + thisIndex + ", frameNum = " + frameNum);
			return -1;
		}
		
		// 마지막 프레임일 때
		if (thisIndex == 9) {
			dto.setTotalScore(dto.getTotalScore() + dto.getScore_list()[18] + dto.getScore_list()[19]);
			System.out.println("경우 3 : " + "thisIndex = " + thisIndex + ", frameNum = " + frameNum);
			return -1;
		}
		
		// 그 다음 프레임이 스트라이크가 아닌 경우
		if (!isStrike(dto.getFrame_list()[thisIndex + 1])) {
			System.out.println("경우2 : " + "thisIndex = " + thisIndex + ", frameNum = " + frameNum + ", 보너스 = " + dto.getFrame_list()[thisIndex + 1]);
			return dto.getFrame_list()[thisIndex + 1];
		}
		
		//그 다음 프레임이 스트라이크인 경우 보너스 계산을 보류
		if (thisIndex <= 8 && thisIndex + 2 > frameNum) {
			System.out.println("경우4 : " + "thisIndex = " + thisIndex + ", frameNum = " + frameNum);
			return -1;
		}
		
		return 10 + dto.getScore_list()[thisIndex * 2 + 4];
	}
	*/
	//스트라이크, 거터, 오픈 판별 메소드
	public boolean isStrike(int roll) {
		return dto.getScore_list()[roll] == 10;
	}
	
	public boolean isSpare(int roll) {
		return dto.getScore_list()[roll] + dto.getScore_list()[roll + 1] == 10;
	}

	public boolean isOpen(int frameScore) {
		if (frameScore < 10)
			return true;
		else
			return false;
	}
	
	//출력 메소드
	public void print() {
		for (int i = 0; i < dto.getScore_list().length; i++) {
			System.out.print(dto.getScore_list()[i] + " |");
		}

		System.out.println("\n------------------------------------프레임 점수 표");
		for (int i = 0; i < dto.getFrame_list().length; i++) {
			System.out.print(" " + dto.getFrame_list()[i] + " |");
		}
		
		System.out.println("\n------------------------------------총 점수 표");
		for (int i = 0; i < dto.getTotal_list().length; i++) {
			System.out.print(" " + dto.getTotal_list()[i] + " |");
		}
		System.out.println();
		System.out.println();
	}
}

