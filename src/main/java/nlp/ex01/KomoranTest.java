package nlp.ex01;

import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class KomoranTest {

	public static void main(String[] args) {
		Komoran komo = new Komoran(DEFAULT_MODEL.LIGHT);
		String str = "★꽁꽁 얼어붙은 한강 위로 고양이가 걸어다닙니다>_<";
		
		//전처리 => 한글,영어,숫자가 아닌 문자는 제거하기
//		str=str.replaceAll("[^가-힣A-Za-z0-9 ]", "");
		str=str.replaceAll("[^가-힣A-Za-z0-9\\s]", "");
		System.out.println("Str: "+str);
		
		KomoranResult result = komo.analyze(str);
		//System.out.println(result);
		String text=result.getPlainText();
		System.out.println("===plain text==========================");
		System.out.println(text);
		
		System.out.println("===token list==========================");
		List<Token> tkList = result.getTokenList();//형태소가 문자열의 어느 위치에 생성됐는지, 품사 등의 정보를 Token에 담아 반환
		//System.out.println(tkList);
		for(Token tk:tkList) {
			System.out.printf("%s\t%s\t(%2d, %2d)\n", tk.getMorph(), tk.getPos(), tk.getBeginIndex(), tk.getEndIndex());
			
		}
		System.out.println("===noun list===========================");
		List<String> nounList=result.getNouns();
		for(String noun: nounList) {
			System.out.println(noun);//NNG(일반명사), NNP(고유명사), NNB(의존명사), VV(동사)
		}
		System.out.println("===morph tag list======================");
		List<String> mList=result.getMorphesByTags("NNG", "NNP", "NNB", "VV");

		mList.forEach(System.out::println);//java 8 함수형 프로그래밍
	}

}
