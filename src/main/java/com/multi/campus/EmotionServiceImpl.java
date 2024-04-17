package com.multi.campus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

@Service
public class EmotionServiceImpl implements EmotionService {

	@Autowired
	private EmotionDAO eDao;

	@Override
	public String emotion(String sentence) {
		Komoran komo = new Komoran(DEFAULT_MODEL.FULL);
		// 리뷰 분석
		KomoranResult res = komo.analyze(sentence);

		List<String> nounList = res.getNouns();

		Set<String> stopWords = new HashSet<>();
		String path="C:\\multicampus\\Spring-workspace\\Spring07_NCPAI\\src\\main\\java\\com\\multi\\campus\\stop.txt";
		//"classpath:com/multi/campus/stop.txt"
		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			stopWords = stream.collect(Collectors.toSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 금칙어 제외처리
		List<String> nounList2 = new ArrayList<String>();
		for (String s : nounList) {
			if (stopWords.contains(s)) {
				continue;
			}
			nounList2.add(s);
		}
		// 금칙어 제외된 명사목록
		System.out.println("불용어 제외한 명사 목록: " + nounList2);
		
		int sum=0;
		for(String word:nounList2) {
			EmotionVO vo =eDao.getEmotion(word);
			System.out.println("vo: "+vo);
			if(vo!=null) {
				sum+=vo.getJumsu();//긍부정 점수 누적
				System.out.println("vo>>: "+vo);
			}
		}
		System.out.println("감성점수 sum: "+sum);
		return (sum<0)?"negative":"positive";
	}

}
