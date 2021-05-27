package com.comin.bowling.vo;
  
import java.io.Serializable;

public class ScoreDTO implements Serializable {
	private int pin = 0;			//사용자가 굴린 핀 갯수
	private int totalScore = 0;		//총 점수
	private int frameScore = 0;		//프레임당 점수	
	private int frameNum = 0;		//프레임 번호
	private int rollIndex = 0;		//투구 횟수(1 or 2)
	private int roll = 0;			//핀을 돌린 횟수(스트라이크시 ++)
	private int[] score_list = new int[21]; // 투구점수
	private int[] frame_list = new int[10]; // 각 프레임 당 투구점수 + 보너스점수
	private int[] total_list = new int[10]; // 각 프레임 당 보너스 점수
	
	public ScoreDTO() {
		super();
	}
	
	public int getPin() {
		return pin;
	}
	
	public void setPin(int pin) {
		this.pin = pin;
	}
	
	public int getTotalScore() {
		return totalScore;
	}
	
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
	public int getFrameScore() {
		return frameScore;
	}
	
	public void setFrameScore(int frameScore) {
		this.frameScore = frameScore;
	}
	
	public int getFrameNum() {
		return frameNum;
	}
	
	public void setFrameNum(int frameNum) {
		this.frameNum = frameNum;
	}
	
	public int getRollIndex() {
		return rollIndex;
	}
	
	public void setRollIndex(int rollIndex) {
		this.rollIndex = rollIndex;
	}
	
	public int getRoll() {
		return roll;
	}
	
	public void setRoll(int roll) {
		this.roll = roll;
	}
	
	public int[] getScore_list() {
		return score_list;
	}
	
	public void setScore_list(int[] score_list) {
		this.score_list = score_list;
	}
	
	public int[] getFrame_list() {
		return frame_list;
	}
	
	public void setFrame_list(int[] frame_list) {
		this.frame_list = frame_list;
	}

	public int[] getTotal_list() {
		return total_list;
	}

	public void setTotal_list(int[] total_list) {
		this.total_list = total_list;
	}
}

