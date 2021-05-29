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
		try {
			assertThat(score.score(2).get(0), is(13));
			System.out.println("oneSpareGame() Test Success!");
		} catch (Exception e) {
			System.out.println("oneSpareGame() Test Fail!");
		}
		
	}
	
	@Test
	public void oneStrikeGame() {
		score.initialization();
		rollStrike();
		score.roll(5);
		score.roll(4);
		try {
			assertThat(score.score(2).get(0), is(19));
			System.out.println("oneStrikeGame() Test Success!");
		} catch (Exception e) {
			System.out.println("oneStrikeGame() Test Fail!");
		}
	}
	
	@Test
	public void gutterGame() {
		score.initialization();
		rollMany(20, 0);
		try {
			assertThat(score.score(10).get(9), is(0));
			System.out.println("gutterGame() Test Success!");
		} catch (Exception e) {
			System.out.println("gutterGame() Test Fail!");
		}
	}
	
	@Test
	public void allOnesGame() {
		score.initialization();
		rollMany(20, 1);
		try {
			assertThat(score.score(10).get(9), is(20));
			System.out.println("allOnesGame() Test Success!");
		} catch (Exception e) {
			System.out.println("allOnesGame() Test Fail!");
		}
	}
	
	@Test
	public void allSpareGame() {
		score.initialization();
		rollMany(21, 5);
		try {
			assertThat(score.score(10).get(9), is(150));
			System.out.println("allSpareGame() Test Success!");
		} catch (Exception e) {
			System.out.println("allSpareGame() Test Fail!");
		}
	}
	
	@Test
	public void perfectGame() {
		score.initialization();
		rollMany(12, 10);
		try {
			assertThat(score.score(10).get(9), is(300));
			System.out.println("perfectGame() Test Success!");
		} catch (Exception e) {
			System.out.println("perfectGame() Test Fail!");
		}
	}
	
	@Test
	public void randomGame() {
		score.initialization();
		score.roll(7);
		score.roll(2);
		rollStrike();
		score.roll(3);
		score.roll(5);
		try {
			assertThat(score.score(3).get(2), is(35));
			System.out.println("randomGame() Test Success!");
		} catch (Exception e) {
			System.out.println("randomGame() Test Fail!");
		}
	}

}
