package com.readers.be3.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.readers.be3.dto.response.OneCommentListDTO;
import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.OneCommentEntity;
import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.exception.ErrorResponse;
import com.readers.be3.exception.ReadersProjectException;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.OneCommentRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.UserInfoRepository;

import jakarta.transaction.Transactional;

@Service
public class OneCommentService {
  
  @Autowired ScheduleInfoRepository scheduleInfoRepository;
  @Autowired OneCommentRepository oneCommentRepository;
  @Autowired UserInfoRepository useInfoRepository;
  @Autowired BookInfoRepository bookRepository;

  @Transactional
  public Map<String,Object> OneCommentAdd(Long uiSeq, Long bookSeq, String comment, Integer score){
    Map<String,Object> map = new HashMap<String, Object>();
    //유저 체크
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));

    BookInfoEntity bookInfoEntity = bookRepository.findByBiSeq(bookSeq);
    if (bookInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found book", bookSeq)));
    
    List<OneCommentEntity> oneCommentEntityList = oneCommentRepository.findByUserInfoEntityAndBookInfoEntity(userInfoEntity, bookInfoEntity);
    if (oneCommentEntityList.size() > 0)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.CREATED, String.format("already wrote oneComment")));
    oneCommentRepository.saveAndFlush(OneCommentEntity.of(comment, score, userInfoEntity, bookInfoEntity));
    map.put("status", HttpStatus.OK);
    map.put("msg", "comment add");
    return map;
  }

  @Transactional
  public Map<String, Object> OneCommentDelete(Long uiSeq, Long oneCommentSeq) {
    Map<String,Object> map = new HashMap<String, Object>();
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));
    OneCommentEntity oneEntity = oneCommentRepository.findByOcSeq(oneCommentSeq);
    if(oneEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found oneComment", uiSeq)));
    oneEntity.setOcStatus(2);
    oneCommentRepository.save(oneEntity);
    map.put("status", HttpStatus.OK);
    map.put("msg", "delete OneComment");
    return map;
  }

  @Transactional
  public Map<String, Object> oneCommentList(Long bookSeq, Pageable pageable){
    Map<String,Object> map = new HashMap<String, Object>();
    BookInfoEntity bookInfoEntity = bookRepository.findByBiSeq(bookSeq);
    if (bookInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found book", bookSeq)));
    Page<OneCommentEntity> commentList = oneCommentRepository.findByBookInfoEntityAndOcStatus(bookInfoEntity,pageable, 1);
    
    if (commentList.isEmpty()){
      map.put("status", HttpStatus.OK);
      map.put("msg", "not found comment");
      return map;
    }
    Page<OneCommentListDTO> onecommentDto = commentList.map(e-> OneCommentListDTO.toDto(e));
    map.put("status", HttpStatus.OK);
    map.put("msg", "ok");
    map.put("commetList", onecommentDto);
    return map;
  }
}
