/**
 * 
 */
package com.comin.bowling.calculator;

import java.util.List;

import com.comin.bowling.vo.GameVO;
import com.comin.bowling.vo.PlayerVO;

public class GameInfo {
	int pseq, frame, turn, remainPin, numberOfPlayer;
	
	// pin을 던질 때 게임 정보를 계산하는 메소드 (프레임, 플레이어 순서, 투구 순서 계산)
	public GameVO gameInfoCalc(GameVO gvo, int rollNum, Integer ball_10_1) {
		pseq = gvo.getPseq();
		frame = gvo.getFrame();
		turn = gvo.getTurn();
		remainPin = gvo.getRemainPin();
		numberOfPlayer = gvo.getNumberOfPlayer();
		gvo.setTotalScore(null);
		
		remainPin -= rollNum;
		
		if(frame < 10) {
			if(turn == 1 && remainPin != 0) {	// 첫번째 투구 시 스트라이크 X
				turn = 2;
			} else if((turn == 1 && remainPin == 0) || turn == 2) {	
			// 첫번째 투구가 스트라이크이거나 두번째 투구까지 쳤을 때
				turn = 1;
				pseq++;
				remainPin = 10;
			}
		} else { 	//10프레임
			if(turn == 1) {
				if(ball_10_1 == 10) {	//첫번째 핀이 스트라이크일 경우
					turn = 2;
					remainPin = 10;
				} else {				//첫번째 핀이 스트라이크가 아닐 경우
					turn = 2;
				}
			} else if(turn == 2) {
				if(remainPin == 0) {	//마지막 프레임이 스페어 or 두번째 핀이 스트라이크
					turn = 3;
					remainPin = 10;
				} else if(remainPin != 0 && ball_10_1 == 10) {	//첫번째 핀 스트라이크 and 두번째 핀 스트라이크 X
					turn = 3;
				} else {
					turn = 1;
					pseq++;
					remainPin = 10;
					gvo.setTotalScore(0);
					// → 각 플레이어 별 플레이어 정보의 최종 총점 업데이트 시점을 구별하기 위해 0으로 셋팅
				}
			} else if(turn == 3) {
				turn = 1;
				pseq++;
				remainPin = 10;
				gvo.setTotalScore(0);
				// → 각 플레이어 별 플레이어 정보의 최종 총점 업데이트 시점을 구별하기 위해 0으로 셋팅
			}
			
		}
		
		// 플레이어 순서가 플레이어 수보다 커지면 순서를 다시 1번으로 돌리고, 프레임을 증가시킴
		if(pseq > numberOfPlayer) {
			pseq = 1;
			frame++;
		}
		
		gvo.setPseq(pseq);
		gvo.setFrame(frame);
		gvo.setTurn(turn);
		gvo.setRemainPin(remainPin);
		
		if(frame > 10) gvo.setProceeding("N");
		else gvo.setProceeding("Y");
		
		return gvo;
	}
	
	// pin을 삭제할 때 게임 정보를 계산하는 메소드
	public GameVO gameInfoDeleteCalc(GameVO gvo, List<PlayerVO> list) {
		pseq = gvo.getPseq();
		frame = gvo.getFrame();
		turn = gvo.getTurn();
		remainPin = gvo.getRemainPin();
		numberOfPlayer = gvo.getNumberOfPlayer();
		gvo.setTotalScore(null);
		
		if(frame == 1 && pseq == 1 && turn == 1) return null;
		
		// 플레이어 순서가 1일 경우 마지막 플레이어로 변경하고 프레임은 감소시킨다.
		// 플레이어 순서가 1이 아닐 경우에는 플레이어 순서를 차례로 감소
		if(turn == 1) {
			if(pseq != 1) {
				pseq--;
				if(frame == 10) {
					gvo.setTotalScore(1);
					// → 각 플레이어 별 플레이어 정보의 최종 총점 업데이트 시점을 구별하기 위해 1로 셋팅
				}
			} else {
				pseq = numberOfPlayer;
				frame--;
				if(frame == 10) {
					gvo.setTotalScore(1);
					// → 각 플레이어 별 플레이어 정보의 최종 총점 업데이트 시점을 구별하기 위해 1로 셋팅
				}
			}
		}
		
		PlayerVO pvo = list.get(pseq-1);
		Integer ball_10_1 = pvo.getBall_10_1();
		Integer ball_10_2 = pvo.getBall_10_2();
		Integer ball_10_3 = pvo.getBall_10_3();
		
		if(frame < 10) {	//1 ~ 9프레임
			if(turn == 1) {		//두번째 투구를 삭제했을 경우(첫번째 투구는 입력 완료)
				if(pvo.get_Ball(frame, 1) == 10) {	//첫번째 투구가 스트라이크일 경우
					turn = 1;
					remainPin = 10;
				} else {	// 첫번째 투구가 스트라이크가 아닐 경우
					turn = 2;
					remainPin = 10 - pvo.get_Ball(frame, 1);
				}
			} else { 	// 첫번째 투구를 삭제했을 경우(전 프레임까지만 입력 완료)
				turn = 1; 
				remainPin = 10;
			}
		} else { 	//10프레임
			if(turn == 1) {
				if(ball_10_3 != null) {		//10프레임의 3투구(보너스)를 지울때
					turn = 3;
					if(ball_10_1 + ball_10_2 == 10 || 	//10 프레임이 스페어 이거나 10 프레임이 연속 스트라이크일 경우
					   ball_10_1 + ball_10_2 == 20) remainPin = 10;
					else remainPin = 10 - ball_10_2;	//10프레임의 1투구는 스트라이크지만 2투구가 스트라이크 X
				} else {	// 보너스 투구가 없는 10프레임의 2투구를 지울때
					turn = 2;
					remainPin = 10 - pvo.get_Ball(frame, 1);
				}
				
			} else if(turn == 3) {	// 보너스 투구가 있는 10프레임의 2투구를 지울때
				turn = 2;
				if(ball_10_1 == 10) remainPin = 10;
				else remainPin = 10 - pvo.get_Ball(frame, 1);
				
			} else if(turn == 2) {		// 10프레임의 1투구를 지울때
				turn = 1;
				remainPin = 10;
			}
		}
		
		gvo.setProceeding("Y");
		gvo.setPseq(pseq);
		gvo.setFrame(frame);
		gvo.setTurn(turn);
		gvo.setRemainPin(remainPin);
		
		return gvo;
	}
}
