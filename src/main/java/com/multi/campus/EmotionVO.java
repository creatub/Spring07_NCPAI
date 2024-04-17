package com.multi.campus;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="emotion_dic")
public class EmotionVO {

	@Id
	private String id;
	
	private String word;
	
	private int jumsu;
}
