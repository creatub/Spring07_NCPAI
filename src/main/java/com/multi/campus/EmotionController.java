package com.multi.campus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmotionController {
	
	@Autowired
	private EmotionService eService;

	@GetMapping("/emotionForm")
	public String showForm() {
		
		return "emotion";
	}
	
	@GetMapping("/emotion")
	public String emotionResult(String sentence, Model m) {
		System.out.println("sentense: "+sentence);
		
		String res = eService.emotion(sentence);//positive, negative
		m.addAttribute("result",res);
		return "emotion_result";
	}
}
