package com.readers.be3.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.image.ArticleImgEntity;

public interface ArticleImgRepository extends JpaRepository<ArticleImgEntity, Long> {
    
}
