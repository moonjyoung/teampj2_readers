package com.readers.be3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.readers.be3.dto.response.OneCommentListDTO;
import com.readers.be3.entity.OneCommentEntity;
import com.readers.be3.repository.OneCommentRepository;

@Service
public class AppScheduleService {
  
  @Autowired RedisTemplate<String,String> redisTemplate;
  @Autowired OneCommentRepository oneCommentRepository;

  public void oneCommentViewsSchedule(){
  final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
   List<OneCommentEntity> commentList = oneCommentRepository.findByOcViewsGreaterThan(100L);

    for(OneCommentEntity data : commentList){
      String value = valueOperations.get("OneCommentSeq :/" + data.getOcSeq());
      if (value.length() > 1){
        data.setOcViews(data.getOcViews() + Long.parseLong(value));
        valueOperations.set("OneCommentSeq :/" + data.getOcSeq(), "0");
        oneCommentRepository.save(data);
      }
    }

  }
}
