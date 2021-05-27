package com.comin.bowling;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.comin.bowling.calculator.Score;

public class ScoreTest {
	private Score score;
	ArrayList<Integer> list;
	
	@Before
	public void setUp() {
		score = new Score();
		list = new ArrayList<Integer>();
	}
	
	private void rollSpare() {
		score.roll(5);
		score.roll(5);
	}
	
	private void rollStrike() {
		score.roll(10);
		score.roll(null);
	}
	
	private void rollMany(int size, int pin) {
		for (int i = 0; i < size; i++) {
			score.roll(pin);
			if(pin == 10 && i < 9) score.roll(null);
		}
	}
	
	@Test
	public void canRoll() {
		score.initialization();
		score.roll(0);
	}
	
	@Test
	public void oneSpareGame() {
		score.initialization();
		rollSpare();
		score.roll(3);
		score.roll(null);
		assertThat(score.score(2).get(0), is(13));
	}
	
	@Test
	public void oneStrikeGame() {
		score.initialization();
		rollStrike();
		score.roll(5);
		score.roll(4);
		assertThat(score.score(2).get(0), is(19));
	}
	
	@Test
	public void gutterGame() {
		score.initialization();
		rollMany(20, 0);
		assertThat(score.score(10).get(9), is(0));
	}
	
	@Test
	public void allOnesGame() {
		score.initialization();
		rollMany(20, 1);
		assertThat(score.score(10).get(9), is(20));
	}
	
	@Test
	public void allSpareGame() {
		score.initialization();
		rollMany(21, 5);
		assertThat(score.score(10).get(9), is(150));
	}
	
	@Test
	public void perfectGame() {
		score.initialization();
		rollMany(12, 10);
		assertThat(score.score(10).get(9), is(300));
	}
	
	@Test
	public void randomGame() {
		score.initialization();
		score.roll(7);
		score.roll(2);
		rollStrike();
		score.roll(3);
		score.roll(5);
		assertThat(score.score(3).get(2), is(35));
	}

}
