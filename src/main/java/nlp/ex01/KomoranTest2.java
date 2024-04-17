package nlp.ex01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

public class KomoranTest2 {

	static Logger log =LoggerFactory.getLogger(KomoranTest2.class); 
	static Komoran komo=new Komoran(DEFAULT_MODEL.FULL);
	
	public static void main(String[] args) {
		String str="눈이 부시게 푸르른 날은 그리운 사람을 그리워 하자. \r\n저기 저기 저, 가을 꽃 자리 초록이 지쳐 단풍 드는데 ";
		   str+="눈이 내리면 어이 하리야 봄이 또 오면 어이 하리야 내가 죽고서 네가 산다면! 네가 죽고서 내가 산다면 ?";
		   str+="눈이 부시게 푸르른 날은 그리운 사람을 그리워 하자.  [서정주 푸르른 날]";
		log.info("str={}",str);
		
		List<String> arr = getWordNouns(str);
		
		Map<String, Integer> wordMap=getWordCount(arr);
		log.info("wordMap={}",wordMap);
		
		List<WordCount> wList = getWordCountSort(wordMap);
		
		log.info("wList={}", wList);
		
	}//-----------------
	
	
	/*단어 빈도수 (눈,5)*/
	public static Map<String, Integer> getWordCount(List<String> arr) {
		if(arr==null) {
			arr=new ArrayList<>();
		}
		//List에 존재하는 중복되는 단어들의 중복단어를 제거하기 위해 Set타입에 데이터를 저장하자
		Set<String> set = new HashSet<>(arr);
		Map<String, Integer> wordMap = new HashMap<>();//key: 단어 value: 빈도수
		
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String word= it.next();//중복이 제거된 단어를 꺼낸다
			//단어의 빈도수 가져오기
			int cnt=Collections.frequency(arr, word);
			wordMap.put(word, cnt);
		}//------
		return wordMap;
	}//---------------
	//wordMap에서 빈도수 내림차순으로 sorting해서 WordCount에 담아 ArrayList로 반환하는 메서드
	public static List<WordCount> getWordCountSort(Map<String,Integer> wordMap){
		
		PriorityQueue<WordCount> pq=new PriorityQueue<>();
		Set<String> wordSet=wordMap.keySet();
		for(String word:wordSet) {
			int count = wordMap.get(word);//빈도수
			pq.add(new WordCount(word,count));//빈도수 내림차순으로 저장
		}
		List<WordCount> list = new ArrayList<>();
		while(!pq.isEmpty()) {
			WordCount wc=pq.poll();
			if(wc!=null&&wc.getCount()>1) {//빈도수가 2개 이상인 단어만 추가
				list.add(wc);
			}
		}
		return list;
	}//--------------

	public static List<String> getWordNouns(String str) {
		//1. 전처리 - 한글, 알파벳, 숫자, 공백 문자가 아닌 문자는 빈문자열로 치환
		str=str.replaceAll("[^가-힣A-Za-z0-9\\s]", "");
		
		//2. 앞 뒤 공백문자 제거
		str=str.trim();
		log.info("==========전처리 후=========");
		log.info(str);
		
		//3. 형태소 분석
		KomoranResult result = komo.analyze(str);
		
		//4. 분석 결과 중 명사만 가져오기
		List<String> nounList=result.getNouns();
		log.info("nounList={}",nounList);
		
		String str2=result.getPlainText();
		log.info("str2={}", str2);
		

		return nounList;
	}

}
