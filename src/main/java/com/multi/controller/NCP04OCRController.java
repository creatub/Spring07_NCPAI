package com.multi.controller;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.multi.service.OCRService;

@RestController
public class NCP04OCRController {

	@Autowired
	private OCRService ocrService;
	
	@GetMapping("/ocrform")
	public ModelAndView ocrForm() {
		return new ModelAndView("clova_ocrForm");
	}
	
	@PostMapping("/ocrEnd")
	public ModelMap ocrProcess(@RequestParam("imgFile") MultipartFile mfile, HttpSession ses) {
		//1. 이미지 파일 서버에 업로드 처리
		String upDir = ses.getServletContext().getRealPath("/upload");
		String fname = mfile.getOriginalFilename();
		File file= new File(upDir, fname);
		String result="";
		try {
			mfile.transferTo(file);//업로드 처리
		}catch(Exception e) {
			System.out.println("업로드 실패: "+e);
			result=e.toString();
		}
		//2. 서비스에 이미지 파일 절대경로 전달
		String filePath=file.getAbsolutePath();
		result=ocrService.extractTextFromImage(filePath);

		ModelMap map=new ModelMap();
		map.put("result", result);
		return map;
	}
	
}//////////////
