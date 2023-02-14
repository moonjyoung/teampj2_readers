package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.ArticleInfoEntity;

public interface ArticleInfoRepository extends JpaRepository<ArticleInfoEntity, Long>{
    
}
