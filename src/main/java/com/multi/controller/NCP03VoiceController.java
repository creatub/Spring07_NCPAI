package com.multi.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@PropertySource("classpath:apiKey.properties")
public class NCP03VoiceController {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${csr_clientId}")
	private String clientId;
	
	@Value("${csr_clientSecret}")
	private String clientSecret;
	
	@GetMapping("/voiceform")
	public ModelAndView voiceForm() {
		
		return new ModelAndView("clova_voice");
	}
	
	@PostMapping("/voiceToText")
	public ModelMap voiceToText(String content, HttpSession ses){//JSON으로 할때는 ModelMap으로
		log.info("content={}",content);
		String upDir=ses.getServletContext().getRealPath("/upload");
		log.info("upDir={}",upDir);
		String fileName="mp3파일명 들어올 예정";
		//----------------------------------------
		fileName = getNaverVoice(content,upDir);
		//----------------------------------------
		ModelMap map = new ModelMap();
		map.put("fileName", fileName);
		return map;
	}

	private String getNaverVoice(String content, String upDir) {
		try {
            String text = URLEncoder.encode(content, "UTF-8"); // 13자
            String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            // post request
            String postParams = "speaker=njinho&volume=0&speed=0&pitch=0&format=mp3&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());//네이버와 연결된 출력 스트림
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();//네이버가 보내오는 음성파일을 듣는 스트림
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 mp3 파일 생성
                String tempname = Long.valueOf(new Date().getTime()).toString();
                File f = new File(upDir, tempname + ".mp3");
                f.createNewFile();//임시파일명의 파일을 생성. 아직 내용이 들어있진 않음
                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {//네이버가 보내온 음성파일을 bytes배열에 담고 
                    outputStream.write(bytes, 0, read);//tempname.mp3 파일에 bytes배열에 담긴 데이터를 출력
                    //was서버 upload 디렉토리에 파일형태로 저장된다.
                }
                is.close();
                return f.getName();//생성된 mp3 파일명을 반환
            } else {  // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
                return "error "+response.toString();//에러 메시지 반환
            }
        } catch (Exception e) {
            System.out.println(e);
            return "error "+e.toString();
        }
		
	}
}/////////////////////////////////////////////////////////
