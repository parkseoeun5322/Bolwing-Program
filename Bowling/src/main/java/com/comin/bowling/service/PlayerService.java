package com.comin.bowling.service;

import java.util.HashMap;
import java.util.List;

import com.comin.bowling.vo.GameVO;
import com.comin.bowling.vo.PlayerVO;

public interface PlayerService {
	public int player_insert(PlayerVO vo);							//플레이어 정보 등록
	public List<PlayerVO> all_list(int seq);						//게임번호별 플레이어 정보 리스트 조회
	public void update_roll(GameVO vo);								//핀 점수 업데이트
	public void delete_frame(GameVO vo);							//핀 점수 삭제
	public void update_totalScore(HashMap<String, Object> map);		//10프레임까지의 총점이 계산될 때 총점 업데이트
	public Integer player_totalScore(HashMap<String, Object> map);	//총점 조회
	public List<PlayerVO> ranking_list();							//랭킹 리스트 불러오기
}
