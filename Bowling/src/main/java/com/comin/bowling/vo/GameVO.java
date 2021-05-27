package com.comin.bowling.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

// 게임 정보 관련 Class

@Getter
@Setter
public class GameVO {
	private int gseq;				//게임 고유 번호
	private String firstPlayer;		//대표 플레이어 이름
	private int numberOfPlayer;		//게임 인원 수
	private String proceeding;		//게임 진행 여부
	private Date startDate;			//게임 시작 날짜
	private int frame = 1;			//현재 프레임
	private int turn = 1;			//첫번째 투구 or 두번째 투구
	private int remainPin = 10;		//남아있는 핀 갯수
	private int pseq = 1;			//현재 플레이어 번호
	private Integer roll;			//현재 친 핀 갯수
	
	private Integer totalScore;		//0 : 핀 입력이 완료됐을 때 총점 DB 업데이트하기 위해 구별
									//1 : 처음 핀을 삭제했을 때 총점 DB 초기화를 하기 위해 구별
									//null : 게임 진행 중 기본값
									//최종 총점 : 총점 DB가 업데이트됐을 때(게임 종료) 최종 총점 할당
	
	private String playerName;		//현재 게임을 진행중인 플레이어 이름
}
