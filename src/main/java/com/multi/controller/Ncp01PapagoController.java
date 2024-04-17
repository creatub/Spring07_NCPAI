package com.multi.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@PropertySource("classpath:apiKey.properties")
public class Ncp01PapagoController {

   Logger log = LoggerFactory.getLogger(getClass());

   private String url = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";

   @Value("${papago_clientId}") // apiKey.properties파일에서 papago_clientId에 해당하는 값을 찾아 주입해준다.
   private String clientId;
   @Value("${papago_clientSecret}")
   private String clientSecret;

   @GetMapping("/papago_form")
   public void papago_form() {

   }

   @PostMapping("/papago_trans")
   @ResponseBody
   public String papago_translate(String str){
      System.out.println("str: " + str);
      try {
            String text=URLEncoder.encode(str, "UTF-8");
            URL ur=new URL(url);
            URLConnection urlCon=ur.openConnection();
            HttpURLConnection con=(HttpURLConnection)urlCon;
            //http방식으로 네이버에 요청을 보내자
            con.setRequestMethod("POST"); // POST방식으로 요청
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            
            //request body에 포함시킬 파라미터 데이터
            String param = "source=ko&target=en&text="+text;
            
            //출력 스트림을 이용해 네이버로 전송한다.
            con.setDoOutput(true);
            
            //파라미터 데이터를 포함한 요청을 전송
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.writeBytes(param);
            dos.flush();
            dos.close();
            
            int resCode=con.getResponseCode();//응답 코드
            BufferedReader br = null;
            if(resCode==200) {//200 OK일 경우
            	// 1바이트인 애들을 2바이트로 조합해주는 InputStreamReader를 Buffer에 차곡차곡 쌓아서 라인 단위로 읽게 해주는
            	// Filter Stream
            	br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else {//에러 발생시
            	br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            
            String line="";
            StringBuffer buf = new StringBuffer();
            while((line=br.readLine())!=null) {
            	buf.append(line);
            }
            br.close();
            String responseData = buf.toString();//응답 데이터 (json 형태로 온다)
            return responseData;
            
      } catch (Exception e) {
    	  e.printStackTrace();
    	  return "{\"error\":\" "+e.getMessage()+" \"}";
      }

   }

   @GetMapping("/test")
   public String testPapago() {
      // log.info("clientId={}",clientId);
      // log.info("clientSecret={}",clientSecret);
      try {
         String text = URLEncoder.encode("만나서 반갑습니다.", "UTF-8");
         String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
         URL url = new URL(apiURL);
         HttpURLConnection con = (HttpURLConnection) url.openConnection();
         con.setRequestMethod("POST");
         con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
         con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
         // post request
         String postParams = "source=ko&target=ja&text=" + text;
         con.setDoOutput(true);
         DataOutputStream wr = new DataOutputStream(con.getOutputStream());
         wr.writeBytes(postParams);
         wr.flush();
         wr.close();
         int responseCode = con.getResponseCode();
         BufferedReader br;
         if (responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
         } else { // 오류 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
         }
         String inputLine;
         StringBuffer response = new StringBuffer();
         while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
         }
         br.close();
         System.out.println(response.toString());
      } catch (Exception e) {
         System.out.println(e);
      }

      return "home";
   }
}