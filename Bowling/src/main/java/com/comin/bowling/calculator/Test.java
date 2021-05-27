package com.comin.bowling.calculator;

import com.comin.bowling.vo.ScoreDTO;

public class Test {
	
	public static void main(String[] args) {
		random();
		
		gutter();
		
		perpect();
		
		allSpare();
		
		//random_play();

		test3();
		
	}
	
	public static void random() {
		ScoreDTO dto = new ScoreDTO();
		Game game = new Game(dto);
		game.roll(7);
		game.roll(2);
		game.roll(3);
		game.roll(5);
		
		if(dto.getTotalScore() == 17) {
			System.out.println("임의 → total_score() 테스트 성공");
		} else {
			System.out.println("임의 실패! 총점 : " + dto.getTotalScore());
		}
		
		game.print();
	}
	
	public static void gutter() {
		ScoreDTO dto = new ScoreDTO();
		Game game = new Game(dto);
		
		for (int i = 0; i < 20; i++) {
			game.roll(0);
		}
		
		if(dto.getTotalScore() == 0) {
			System.out.println("거터 → total_score() 테스트 성공");
		}
		
		game.print();
	}
	
	public static void perpect() {
		ScoreDTO dto = new ScoreDTO();
		Game game = new Game(dto);
		
		for (int i = 0; i < 12; i++) {
			game.roll(10);
		}
		
		if(dto.getTotalScore() == 300) {
			System.out.println("퍼펙트 → total_score() 테스트 성공");
		} else {
			System.out.println("퍼펙트 실패! 총점 : " + dto.getTotalScore());
		}
		
		game.print();
	}
	
	public static void allSpare() {
		ScoreDTO dto = new ScoreDTO();
		Game game = new Game(dto);
		
		for (int i = 0; i < 21; i++) {
			game.roll(5);
		}
		
		if(dto.getTotalScore() == 150) {
			System.out.println("올스페어 → total_score() 테스트 성공");
		} else {
			//System.out.println("올스페어 실패! 총점 : " + game.getTotalScore());
			System.out.println("올스페어 실패! = " + dto.getTotalScore());
		}
		game.print();
	}
	
	public static void test3() {
		ScoreDTO dto = new ScoreDTO();
		Game game = new Game(dto);
		
		game.roll(7);
		game.roll(2);
		game.roll(10);
		game.roll(3);
		game.roll(5);
		game.roll(7);
		game.roll(2);
		game.roll(10);
		game.roll(3);
		game.roll(5);
		game.roll(7);
		game.roll(2);
		game.roll(10);
		game.roll(3);
		game.roll(5);
		game.roll(10);
		game.roll(2);
		game.roll(3);
		
		if(dto.getTotalScore() == 120) System.out.println("test3() 테스트 성공!!");
		else System.out.println("테스트 실패!! 총점 : " + dto.getTotalScore());
		game.print();
		
		//9 / 27 / 35 / 44 / 62 / 70 / 79 / 97 / 105 / 120
	}
}

