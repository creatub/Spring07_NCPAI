package nlp.ex01;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WordCount implements Comparable<WordCount>{
	private String word;//단어
	private int count;//빈도수
	
	@Override
	public int compareTo(WordCount o) {//빈도수 내림차순
		
		return o.count - this.count;//내림차순
	}
}
