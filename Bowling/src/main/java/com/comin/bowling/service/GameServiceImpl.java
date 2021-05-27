package com.comin.bowling.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comin.bowling.vo.GameVO;

@Service
public class GameServiceImpl implements GameService {
	@Autowired GameDAO dao;
	
	@Override
	public List<GameVO> game_list() {
		return dao.game_list();
	}

	@Override
	public int game_insert(GameVO vo) {
		return dao.game_insert(vo);
	}

	@Override
	public void game_update(GameVO vo) {
		dao.game_update(vo);
	}

	@Override
	public GameVO game_info(int gseq) {
		return dao.game_info(gseq);
	}

	@Override
	public int game_reset(int gseq) {
		return dao.game_reset(gseq);
	}

}
