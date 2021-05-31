package com.comin.bowling.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerVO {
	private int rank;				//랭킹 순위(DB X)
	private String firstPlayer;		//대표 플레이어 이름(DB X)
	private int numberOfPlayer;		//게임 인원 수(DB X)
	private Date startDate;			//게임 시작날짜(DB X)
	
	private String gameover;		//플레이어별 게임 종료 여부(DB X)

	private Integer pseq;			//플레이어 번호
	private Integer gseq;			//게임 고유 번호
	private String name;			//플레이어 이름
	private Integer ball_1_1;
	private Integer ball_1_2;
	private Integer ball_2_1;
	private Integer ball_2_2;
	private Integer ball_3_1;
	private Integer ball_3_2;
	private Integer ball_4_1;
	private Integer ball_4_2;
	private Integer ball_5_1;
	private Integer ball_5_2;
	private Integer ball_6_1;
	private Integer ball_6_2;
	private Integer ball_7_1;
	private Integer ball_7_2;
	private Integer ball_8_1;
	private Integer ball_8_2;
	private Integer ball_9_1;
	private Integer ball_9_2;
	private Integer ball_10_1;
	private Integer ball_10_2;
	private Integer ball_10_3;
	private Integer total_score;

	public Integer get_Ball(int frame, int turn) {
		if(frame == 1) {
			if(turn == 1) return ball_1_1;
			else return ball_1_2;
		} else if(frame == 2) {
			if(turn == 1) return ball_2_1;
			else return ball_2_2;
		} else if(frame == 3) {
			if(turn == 1) return ball_3_1;
			else return this.ball_3_2;
		} else if(frame == 4) {
			if(turn == 1) return ball_4_1;
			else return ball_4_2;
		} else if(frame == 5) {
			if(turn == 1) return ball_5_1;
			else return ball_5_2;
		} else if(frame == 6) {
			if(turn == 1) return ball_6_1;
			else return this.ball_6_2;
		} else if(frame == 7) {
			if(turn == 1) return ball_7_1;
			else return ball_7_2;
		} else if(frame == 8) {
			if(turn == 1) return ball_8_1;
			else return this.ball_8_2;
		} else if(frame == 9) {
			if(turn == 1) return ball_9_1;
			else return this.ball_9_2;
		} else {
			if(turn == 1) return ball_10_1;
			else if(turn == 2) return ball_10_2;
			else return this.ball_10_3;
		}
	}
	
	public void set_Ball(int frame, int turn, Integer roll) {
		if(frame == 1) {
			if(turn == 1) this.ball_1_1 = roll;
			else this.ball_1_2 = roll;
		} else if(frame == 2) {
			if(turn == 1) this.ball_2_1 = roll;
			else this.ball_2_2 = roll;
		} else if(frame == 3) {
			if(turn == 1) this.ball_3_1 = roll;
			else this.ball_3_2 = roll;
		} else if(frame == 4) {
			if(turn == 1) this.ball_4_1 = roll;
			else this.ball_4_2 = roll;
		} else if(frame == 5) {
			if(turn == 1) this.ball_5_1 = roll;
			else this.ball_5_2 = roll;
		} else if(frame == 6) {
			if(turn == 1) this.ball_6_1 = roll;
			else this.ball_6_2 = roll;
		} else if(frame == 7) {
			if(turn == 1) this.ball_7_1 = roll;
			else this.ball_7_2 = roll;
		} else if(frame == 8) {
			if(turn == 1) this.ball_8_1 = roll;
			else this.ball_8_2 = roll;
		} else if(frame == 9) {
			if(turn == 1) this.ball_9_1 = roll;
			else this.ball_9_2 = roll;
		} else {
			if(turn == 1) this.ball_10_1 = roll;
			else if(turn == 2) this.ball_10_2 = roll;
			else this.ball_10_3 = roll;
		}
	}
}
