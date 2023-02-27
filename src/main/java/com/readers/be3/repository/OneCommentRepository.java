package com.readers.be3.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.OneCommentEntity;
import com.readers.be3.entity.UserInfoEntity;

public interface OneCommentRepository extends JpaRepository<OneCommentEntity, Long>{
  OneCommentEntity findByOcSeq(Long ocSeq);

  Page<OneCommentEntity> findByBookInfoEntityAndOcStatus(BookInfoEntity bookInfoEntity, Pageable pageable, Integer ocStatus);
  List<OneCommentEntity> findByBookInfoEntity(BookInfoEntity bookInfoEntity);

  List<OneCommentEntity> findByUserInfoEntityAndBookInfoEntity(UserInfoEntity userInfoEntity, BookInfoEntity bookInfoEntity);

  OneCommentEntity findByOcSeqAndUserInfoEntity(Long oneCommentSeq, UserInfoEntity userInfoEntity);

  List<OneCommentEntity> findByOcViewsGreaterThan(Long ocViews);

  List<OneCommentEntity> findByOcViewsGreaterThanAndOcRegDtAfter(long l, LocalDateTime onedayago);

  List<OneCommentEntity> findByOcViewsGreaterThanAndOcRegDtAfterAndOcStatus(long l, LocalDateTime oneDayBefore, Integer status);
}
