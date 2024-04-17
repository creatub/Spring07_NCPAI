package com.multi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multi.service.SampleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final SampleService sampleService;
	
	@RequestMapping("/index")
	public String test() {
		
		return "home";
	}
	
	@GetMapping("/mybatis_test")
	public void mybatis_test(Model m) {
		int count =sampleService.getTableCount();
		m.addAttribute("tableCount", count);
	}
}
