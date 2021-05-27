package com.comin.bowling.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comin.bowling.vo.GameVO;
import com.comin.bowling.vo.PlayerVO;

@Service
public class PlayerServiceImpl implements PlayerService {
	@Autowired PlayerDAO dao;
	
	@Override
	public int player_insert(PlayerVO vo) {
		return dao.player_insert(vo);
	}

	@Override
	public List<PlayerVO> all_list(int seq) {
		return dao.all_list(seq);
	}

	@Override
	public void update_roll(GameVO vo) {
		dao.update_roll(vo);
	}

	@Override
	public void delete_frame(GameVO vo) {
		dao.delete_frame(vo);
	}

	@Override
	public void update_totalScore(HashMap<String, Object> map) {
		dao.update_totalScore(map);
	}

	@Override
	public List<PlayerVO> ranking_list() {
		return dao.ranking_list();
	}

	@Override
	public Integer player_totalScore(HashMap<String, Object> map) {
		return dao.player_totalScore(map);
	}
}
