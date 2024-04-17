package com.multi.campus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class EmotionDAOImpl implements EmotionDAO {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public EmotionVO getEmotion(String word) {
		Query query = new Query(new Criteria("word").is(word));
		EmotionVO vo=mongoTemplate.findOne(query, EmotionVO.class, "emotion_dic");
		return vo;
	}

}
