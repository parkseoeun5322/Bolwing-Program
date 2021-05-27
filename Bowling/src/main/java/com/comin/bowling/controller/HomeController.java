package com.comin.bowling.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.comin.bowling.calculator.Game;
import com.comin.bowling.vo.ScoreDTO;

@Controller			//Controller를 의미하는 Annotation
public class HomeController {			// "/"의 요청에 대한 해당 메소드(home)가 실행됨
	Game[] game;
	ScoreDTO[] dto;
	ScoreDTO[] dto2;
	FileOutputStream fos;
	ObjectOutputStream oos;
	FileInputStream fis;
	ObjectInputStream ois;
	int index = 0;
	String url = "C:\\Users\\qazpl\\Desktop\\Education\\springWorkspace\\Bowling\\src\\main\\webapp\\resources\\txt\\";
	boolean bonus = false;
	/*
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		File dir = new File(url);
		File files[] = dir.listFiles();
		//System.out.println("파일 갯수 : " + files.length);
		
		
		dto2 = new ScoreDTO[files.length];
		System.out.println("dto2의 갯수 : " + dto2.length);
		
		for (int i = 0; i < files.length; i++) {
			dto2[i] = new ScoreDTO();
			try {
				fis = new FileInputStream(url + "game" + i + ".txt");
				ois = new ObjectInputStream(fis);
				dto2[i] = (ScoreDTO)ois.readObject();
			} catch (Exception e) {
				System.out.println("초반 불러오기 실패");
			}
		}
		
		model.addAttribute("dto2", dto2);
		
		return "home";			
		// 리턴되는 뷰 이름에 해당하는 파일을 찾아감(.jsp는 생략)
		// → WEB-INF/views.home.jsp
	}
	
	@RequestMapping("/roll")
	public String roll(Model model, int roll, int playNum) {
		
		//01 - 0 	01 - 0
		//23 - 1	23 - 1
		//45 - 0	45 - 2	
		//67 - 1	67 - 0
		if(index / 2 == playNum) index = 0;
		//System.out.println("플레이어 " + (index/2) + "의 roll 횟수 : " + game[index/2].getDto().getRoll());
		if(bonus && game[index/2].getDto().getRoll() <= 19) {
			//System.out.println("roll 횟수 : " + game[index/2].getDto().getRoll());
			game[index/2].roll(roll);
			bonus = false;
		}
		
		//System.out.println("보너스 : " + bonus);
		
		if(game[index/2].getDto().getRoll() <= 18) {
			//System.out.println("보통 : bonus : " + bonus);
			game[index/2].roll(roll);
		}
		
		if(game[index/2].getDto().getRoll() <= 19) bonus = game[index/2].isBonusRoll();
		
		
		if(roll == 10 && game[index/2].getDto().getFrameNum() != 9) index++;
		
		//System.out.println("index = " + index);
		//game[index / 2].print();
		
		try {
			fos = new FileOutputStream(url + "game" + index/2 + ".txt");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(dto[index/2]);
			oos.close();
			fos.close();
			
			fis = new FileInputStream(url + "game" + index/2 + ".txt");
			ois = new ObjectInputStream(fis);
			dto2[index/2] = (ScoreDTO)ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("오류!");
		}
		
		model.addAttribute("dto2", dto2);
		
		if(!bonus) index++;
		return "home";
	}
	
	@RequestMapping("/play")
	public String play(HttpSession session, Model model, int playNum) {
		game = new Game[playNum];
		dto = new ScoreDTO[playNum];
		dto2 = new ScoreDTO[playNum];
		index = 0;
		
		for (int i = 0; i < playNum; i++) {
			dto[i] = new ScoreDTO();
			game[i] = new Game(dto[i]);
			
			try {
				fis = new FileInputStream(url + "game" + i + ".txt");
				ois = new ObjectInputStream(fis);
				dto2[index/2] = (ScoreDTO)ois.readObject();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		} //for
		session.setAttribute("playNum", playNum);
		session.setMaxInactiveInterval(1000);
		
		return "home";
	} //play()
	*/
}

