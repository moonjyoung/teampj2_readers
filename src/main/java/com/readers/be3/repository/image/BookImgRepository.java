package com.readers.be3.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.image.BookImgEntity;

public interface BookImgRepository extends JpaRepository<BookImgEntity, Long>{
    public BookImgEntity findByBimgBiSeq(Long bimgBiSeq);
    public BookImgEntity findTopByBimgUri(String bimgUri);
}
