package com.multi.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


//Naver Clova Speech Recognition
@RestController
@PropertySource("classpath:apiKey.properties")
public class Ncp02CSRController {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${csr_clientId}")
	private String clientId;
	
	@Value("${csr_clientSecret}")
	private String clientSecret;
	
	private String url="https://naveropenapi.apigw.ntruss.com/recog/v1/stt";
	
	@GetMapping("/csrform")
	public ModelAndView csrform() {
		log.info("clientId={}", clientId);
		log.info("clientSecret={}", clientSecret);
		
		return new ModelAndView("clova_speech");
	}
	
	@PostMapping("/csrSpeech")
	public ModelMap speechRecognition(@RequestParam("mp3file") MultipartFile mfile,
			String language, HttpSession ses) {
		log.info("language: "+language);
		ServletContext app = ses.getServletContext();
		String upDir = app.getRealPath("/upload");
		log.info("upDir={}", upDir);
		File dir = new File(upDir);
		if(!dir.exists()) {
			dir.mkdirs();//업로드 디렉토리 생성 ==> src/main/webapp/upload
		}
		String fname=mfile.getOriginalFilename();//첨부파일명
		File voiceFile = new File(upDir,fname);
		String result="test";
		try {
			mfile.transferTo(voiceFile);//업로드 처리
			//------------------------------------------
			result=getNaverResponse(voiceFile, language);
			//------------------------------------------
		}catch(Exception e) {
			result=e.toString();
			log.error("error: {}",  e);
		}
		
		
		ModelMap map = new ModelMap();
		map.put("result", result);
		return map;
		
	}

	private String getNaverResponse(File voiceFile, String language) throws Exception{
//		String imgFile = "음성 파일 경로";
//        File voiceFile = new File(imgFile);
//
//        String language = "Kor";        // 언어 코드 ( Kor, Jpn, Eng, Chn )
        String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
        URL url = new URL(apiURL);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
        conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

        OutputStream outputStream = conn.getOutputStream();//네이버로 음성파일을 보내기 위한 출력 스트림 얻기
        FileInputStream inputStream = new FileInputStream(voiceFile);//was에 업로드된 음성파일을 읽는 스트림 생성
        byte[] buffer = new byte[4096];//파일데이터를 담기 위한 배열 4kb
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);//읽어들인 음성파일을 네이버로 내보낸다
        }
        outputStream.flush();
        inputStream.close();
        BufferedReader br = null;
        int responseCode = conn.getResponseCode();
        if(responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {  // 오류 발생
            System.out.println("error!!!!!!! responseCode= " + responseCode);
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        String inputLine;

        if(br != null) {
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("error !!!");
            return "error"+responseCode;
        }
	}
}
