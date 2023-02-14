package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.ArticleCommentEntity;

public interface ArticleCommentRepository extends JpaRepository<ArticleCommentEntity, Long>{
    
}
