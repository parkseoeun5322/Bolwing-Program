package com.comin.bowling.service;

import java.util.List;
import com.comin.bowling.vo.GameVO;

public interface GameService {
	List<GameVO> game_list();		//모든 게임 정보 조회
	int game_insert(GameVO vo);		//게임 정보 등록
	GameVO game_info(int gseq);		//게임 정보 조회
	void game_update(GameVO vo);	//게임 정보 수정
	int game_reset(int gseq);		//게임 정보 초기화
}
