package com.comin.bowling.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.comin.bowling.vo.GameVO;
import com.comin.bowling.vo.PlayerVO;

@Repository
public class PlayerDAO implements PlayerService {
	@Autowired private SqlSession sql;

	@Override
	public int player_insert(PlayerVO vo) {
		return sql.insert("player.mapper.insert", vo);
	}

	@Override
	public List<PlayerVO> all_list(int seq) {
		return sql.selectList("player.mapper.allList", seq);
	}

	@Override
	public void update_roll(GameVO vo) {
		sql.update("player.mapper.updateRoll", vo);
	}

	@Override
	public void delete_frame(GameVO vo) {
		sql.delete("player.mapper.deleteFrame", vo);
	}

	@Override
	public void update_totalScore(HashMap<String, Object> map) {
		sql.update("player.mapper.updateTotalScore", map);
	}

	@Override
	public List<PlayerVO> ranking_list() {
		return sql.selectList("player.mapper.rankingList");
	}

	@Override
	public Integer player_totalScore(HashMap<String, Object> map) {
		return sql.selectOne("player.mapper.totalScore", map);
	}

}
