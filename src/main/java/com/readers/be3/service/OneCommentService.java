package com.readers.be3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.readers.be3.dto.response.OneCommentListDTO;
import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.OneCommentEntity;
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
  public OneCommentEntity OneCommentAdd(Long uiSeq, Long bookSeq, String comment, Integer score){
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
    OneCommentEntity oneCommentEntity = oneCommentRepository.save(OneCommentEntity.of(comment, score, userInfoEntity, bookInfoEntity));
    return oneCommentEntity;
  }

  @Transactional
  public OneCommentEntity OneCommentDelete(Long uiSeq, Long oneCommentSeq) {
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));
    OneCommentEntity oneEntity = oneCommentRepository.findByOcSeqAndUserInfoEntity(oneCommentSeq, userInfoEntity);
    if(oneEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found oneComment", uiSeq)));
    oneEntity.setOcStatus(2);
    OneCommentEntity oneCommentEntity = oneCommentRepository.save(oneEntity);
    return oneCommentEntity;
  }

  @Transactional
  public Page<OneCommentListDTO> oneCommentList(Long bookSeq, Pageable pageable){
    BookInfoEntity bookInfoEntity = bookRepository.findByBiSeq(bookSeq);
    if (bookInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found book", bookSeq)));
    Page<OneCommentEntity> commentList = oneCommentRepository.findByBookInfoEntityAndOcStatus(bookInfoEntity,pageable, 1);
    Page<OneCommentListDTO> onecommentDto = commentList.map(e-> OneCommentListDTO.toDto(e));
    
    if (onecommentDto.isEmpty())
      return onecommentDto;
    return onecommentDto;
  }

  public OneCommentEntity OneCommentUpdate(Long uiSeq, Long oneCommentSeq, String content) {
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));
    OneCommentEntity oneCommentEntity = oneCommentRepository.findByOcSeq(oneCommentSeq);
    if (oneCommentEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found commentSeq", oneCommentSeq)));
    if (oneCommentEntity.getOcComment().equals(content) )
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.CREATED,String.format("content is equals")));
    if(oneCommentEntity.getUserInfoEntity().equals(oneCommentSeq))
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.CREATED,String.format("not my comment")));
    return oneCommentRepository.save(OneCommentEntity.update(oneCommentEntity, content));
  }

  public Object getOneComment(Long oneCommentSeq) {
    // Find a  onecomment and check if it exists or not
    OneCommentEntity oneCommentEntity = oneCommentRepository.findByOcSeq(oneCommentSeq); 
    if (oneCommentEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found commentSeq", oneCommentSeq)));
    
    oneCommentEntity.increaseViews();
    oneCommentRepository.save(oneCommentEntity);
    return null;
  }
}
