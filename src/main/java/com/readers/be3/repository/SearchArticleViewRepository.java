package com.readers.be3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readers.be3.entity.SearchArticleView;
import com.readers.be3.vo.article.SearchNicknameVO;

public interface SearchArticleViewRepository extends JpaRepository<SearchArticleView,Long>{
// 작성자(닉네임)로 게시글 검색
@Query("SELECT a FROM SearchArticleView a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.uiNickname like %:keyword% ")
public Page<SearchNicknameVO> searchNickname(@Param("keyword") String keyword, Pageable pageable);
}
