package com.comin.bowling.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.comin.bowling.vo.GameVO;

@Repository
public class GameDAO implements GameService {
	@Autowired private SqlSession sql;

	@Override
	public List<GameVO> game_list() {
		return sql.selectList("gameInfo.mapper.list");
	}

	@Override
	public int game_insert(GameVO vo) {
		sql.insert("gameInfo.mapper.insert", vo);
		return vo.getGseq();
	}

	@Override
	public GameVO game_info(int gseq) {
		return sql.selectOne("gameInfo.mapper.info", gseq);
	}

	@Override
	public void game_update(GameVO vo) {
		sql.update("gameInfo.mapper.update", vo);
	}

	@Override
	public int game_reset(int gseq) {
		sql.update("gameInfo.mapper.reset", gseq);
		return sql.update("player.mapper.reset", gseq);
	}
}
